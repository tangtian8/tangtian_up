/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.aot;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.aot.generate.GeneratedMethod;
import org.springframework.aot.generate.MethodGenerator;
import org.springframework.aot.generate.MethodNameGenerator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.core.ResolvableType;
import org.springframework.javapoet.AnnotationSpec;
import org.springframework.javapoet.CodeBlock;
import org.springframework.javapoet.CodeBlock.Builder;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Internal code generator used to generate code for a single value contained in
 * a {@link BeanDefinition} property.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 6.0
 */
class BeanDefinitionPropertyValueCodeGenerator {

	static final CodeBlock NULL_VALUE_CODE_BLOCK = CodeBlock.of("null");

	private final MethodGenerator methodGenerator;

	private final List<Delegate> delegates = List.of(
			new PrimitiveDelegate(),
			new StringDelegate(),
			new EnumDelegate(),
			new ClassDelegate(),
			new ResolvableTypeDelegate(),
			new ArrayDelegate(),
			new ManagedListDelegate(),
			new ManagedSetDelegate(),
			new ManagedMapDelegate(),
			new ListDelegate(),
			new SetDelegate(),
			new MapDelegate(),
			new BeanReferenceDelegate()
		);


	BeanDefinitionPropertyValueCodeGenerator(MethodGenerator methodGenerator) {
		this.methodGenerator = methodGenerator;
	}


	CodeBlock generateCode(@Nullable Object value) {
		ResolvableType type = (value != null) ? ResolvableType.forInstance(value)
				: ResolvableType.NONE;
		return generateCode(value, type);
	}

	private CodeBlock generateCode(@Nullable Object value, ResolvableType type) {
		if (value == null) {
			return NULL_VALUE_CODE_BLOCK;
		}
		for (Delegate delegate : this.delegates) {
			CodeBlock code = delegate.generateCode(value, type);
			if (code != null) {
				return code;
			}
		}
		throw new IllegalArgumentException(
				"'type' " + type + " must be supported for instance code generation");
	}


	/**
	 * Internal delegate used to support generation for a specific type.
	 */
	@FunctionalInterface
	private interface Delegate {

		@Nullable
		CodeBlock generateCode(Object value, ResolvableType type);

	}


	/**
	 * {@link Delegate} for {@code primitive} types.
	 */
	private static class PrimitiveDelegate implements Delegate {

		private static final Map<Character, String> CHAR_ESCAPES = Map.of(
				'\b', "\\b",
				'\t', "\\t",
				'\n', "\\n",
				'\f', "\\f",
				'\r', "\\r",
				'\"', "\"",
				'\'', "\\'",
				'\\', "\\\\"
			);


		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof Boolean || value instanceof Integer) {
				return CodeBlock.of("$L", value);
			}
			if (value instanceof Byte) {
				return CodeBlock.of("(byte) $L", value);
			}
			if (value instanceof Short) {
				return CodeBlock.of("(short) $L", value);
			}
			if (value instanceof Long) {
				return CodeBlock.of("$LL", value);
			}
			if (value instanceof Float) {
				return CodeBlock.of("$LF", value);
			}
			if (value instanceof Double) {
				return CodeBlock.of("(double) $L", value);
			}
			if (value instanceof Character character) {
				return CodeBlock.of("'$L'", escape(character));
			}
			return null;
		}

		private String escape(char ch) {
			String escaped = CHAR_ESCAPES.get(ch);
			if (escaped != null) {
				return escaped;
			}
			return (!Character.isISOControl(ch)) ? Character.toString(ch)
					: String.format("\\u%04x", (int) ch);
		}
	}


	/**
	 * {@link Delegate} for {@link String} types.
	 */
	private static class StringDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof String) {
				return CodeBlock.of("$S", value);
			}
			return null;
		}

	}


	/**
	 * {@link Delegate} for {@link Enum} types.
	 */
	private static class EnumDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof Enum<?> enumValue) {
				return CodeBlock.of("$T.$L", enumValue.getDeclaringClass(),
						enumValue.name());
			}
			return null;
		}

	}


	/**
	 * {@link Delegate} for {@link Class} types.
	 */
	private static class ClassDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof Class<?> clazz) {
				return CodeBlock.of("$T.class", ClassUtils.getUserClass(clazz));
			}
			return null;
		}

	}


	/**
	 * {@link Delegate} for {@link ResolvableType} types.
	 */
	private static class ResolvableTypeDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof ResolvableType resolvableType) {
				return ResolvableTypeCodeGenerator.generateCode(resolvableType);
			}
			return null;
		}

	}


	/**
	 * {@link Delegate} for {@code array} types.
	 */
	private class ArrayDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(@Nullable Object value, ResolvableType type) {
			if (type.isArray()) {
				ResolvableType componentType = type.getComponentType();
				int length = Array.getLength(value);
				CodeBlock.Builder builder = CodeBlock.builder();
				builder.add("new $T {", type.toClass());
				for (int i = 0; i < length; i++) {
					Object component = Array.get(value, i);
					if (i != 0) {
						builder.add(", ");
					}
					builder.add("$L", BeanDefinitionPropertyValueCodeGenerator.this
							.generateCode(component, componentType));
				}
				builder.add("}");
				return builder.build();
			}
			return null;
		}

	}


	/**
	 * Abstract {@link Delegate} for {@code Collection} types.
	 */
	private abstract class CollectionDelegate<T extends Collection<?>> implements Delegate {

		private final Class<?> collectionType;

		private final CodeBlock emptyResult;

		public CollectionDelegate(Class<?> collectionType, CodeBlock emptyResult) {
			this.collectionType = collectionType;
			this.emptyResult = emptyResult;
		}

		@Override
		@SuppressWarnings("unchecked")
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (this.collectionType.isInstance(value)) {
				T collection = (T) value;
				if (collection.isEmpty()) {
					return this.emptyResult;
				}
				ResolvableType elementType = type.as(this.collectionType).getGeneric();
				return generateCollectionCode(elementType, collection);
			}
			return null;
		}

		protected CodeBlock generateCollectionCode(ResolvableType elementType, T collection) {
			return generateCollectionOf(collection, this.collectionType, elementType);
		}

		protected final CodeBlock generateCollectionOf(Collection<?> collection,
				Class<?> collectionType, ResolvableType elementType) {
			Builder builder = CodeBlock.builder();
			builder.add("$T.of(", collectionType);
			Iterator<?> iterator = collection.iterator();
			while (iterator.hasNext()) {
				Object element = iterator.next();
				builder.add("$L", BeanDefinitionPropertyValueCodeGenerator.this
						.generateCode(element, elementType));
				if (iterator.hasNext()) {
					builder.add(", ");
				}
			}
			builder.add(")");
			return builder.build();
		}

	}


	/**
	 * {@link Delegate} for {@link ManagedList} types.
	 */
	private class ManagedListDelegate extends CollectionDelegate<ManagedList<?>> {

		public ManagedListDelegate() {
			super(ManagedList.class, CodeBlock.of("new $T()", ManagedList.class));
		}

	}


	/**
	 * {@link Delegate} for {@link ManagedSet} types.
	 */
	private class ManagedSetDelegate extends CollectionDelegate<ManagedSet<?>> {

		public ManagedSetDelegate() {
			super(ManagedSet.class, CodeBlock.of("new $T()", ManagedSet.class));
		}

	}


	/**
	 * {@link Delegate} for {@link ManagedMap} types.
	 */
	private class ManagedMapDelegate implements Delegate {

		private static final CodeBlock EMPTY_RESULT = CodeBlock.of("$T.ofEntries()",
				ManagedMap.class);

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof ManagedMap<?, ?> managedMap) {
				return generateManagedMapCode(type, managedMap);
			}
			return null;
		}

		private <K, V> CodeBlock generateManagedMapCode(ResolvableType type,
				ManagedMap<K, V> managedMap) {
			if (managedMap.isEmpty()) {
				return EMPTY_RESULT;
			}
			ResolvableType keyType = type.as(Map.class).getGeneric(0);
			ResolvableType valueType = type.as(Map.class).getGeneric(1);
			CodeBlock.Builder builder = CodeBlock.builder();
			builder.add("$T.ofEntries(", ManagedMap.class);
			Iterator<Map.Entry<K, V>> iterator = managedMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<?, ?> entry = iterator.next();
				builder.add("$T.entry($L,$L)", Map.class,
						BeanDefinitionPropertyValueCodeGenerator.this
								.generateCode(entry.getKey(), keyType),
						BeanDefinitionPropertyValueCodeGenerator.this
								.generateCode(entry.getValue(), valueType));
				if (iterator.hasNext()) {
					builder.add(", ");
				}
			}
			builder.add(")");
			return builder.build();
		}

	}


	/**
	 * {@link Delegate} for {@link List} types.
	 */
	private class ListDelegate extends CollectionDelegate<List<?>> {

		ListDelegate() {
			super(List.class, CodeBlock.of("$T.emptyList()", Collections.class));
		}

	}


	/**
	 * {@link Delegate} for {@link Set} types.
	 */
	private class SetDelegate extends CollectionDelegate<Set<?>> {

		SetDelegate() {
			super(Set.class, CodeBlock.of("$T.emptySet()", Collections.class));
		}

		@Override
		protected CodeBlock generateCollectionCode(ResolvableType elementType, Set<?> set) {
			if (set instanceof LinkedHashSet) {
				return CodeBlock.of("new $T($L)", LinkedHashSet.class,
						generateCollectionOf(set, List.class, elementType));
			}
			set = orderForCodeConsistency(set);
			return super.generateCollectionCode(elementType, set);
		}

		private Set<?> orderForCodeConsistency(Set<?> set) {
			return new TreeSet<Object>(set);
		}

	}


	/**
	 * {@link Delegate} for {@link Map} types.
	 */
	private class MapDelegate implements Delegate {

		private static final CodeBlock EMPTY_RESULT = CodeBlock.of("$T.emptyMap()",
				Collections.class);

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof Map<?, ?> map) {
				return generateMapCode(type, map);
			}
			return null;
		}

		private <K, V> CodeBlock generateMapCode(ResolvableType type, Map<K, V> map) {
			if (map.isEmpty()) {
				return EMPTY_RESULT;
			}
			ResolvableType keyType = type.as(Map.class).getGeneric(0);
			ResolvableType valueType = type.as(Map.class).getGeneric(1);
			if (map instanceof LinkedHashMap<?, ?>) {
				return generateLinkedHashMapCode(map, keyType, valueType);
			}
			map = orderForCodeConsistency(map);
			boolean useOfEntries = map.size() > 10;
			CodeBlock.Builder builder = CodeBlock.builder();
			builder.add("$T" + ((!useOfEntries) ? ".of(" : ".ofEntries("), Map.class);
			Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<K, V> entry = iterator.next();
				CodeBlock keyCode = BeanDefinitionPropertyValueCodeGenerator.this
						.generateCode(entry.getKey(), keyType);
				CodeBlock valueCode = BeanDefinitionPropertyValueCodeGenerator.this
						.generateCode(entry.getValue(), valueType);
				if (!useOfEntries) {
					builder.add("$L, $L", keyCode, valueCode);
				}
				else {
					builder.add("$T.entry($L,$L)", Map.class, keyCode, valueCode);
				}
				if (iterator.hasNext()) {
					builder.add(", ");
				}
			}
			builder.add(")");
			return builder.build();
		}

		private <K, V> Map<K, V> orderForCodeConsistency(Map<K, V> map) {
			return new TreeMap<>(map);
		}

		private <K, V> CodeBlock generateLinkedHashMapCode(Map<K, V> map,
				ResolvableType keyType, ResolvableType valueType) {
			GeneratedMethod method = BeanDefinitionPropertyValueCodeGenerator.this.methodGenerator
					.generateMethod(MethodNameGenerator.join("get", "map"))
					.using(builder -> {
						builder.addAnnotation(AnnotationSpec
								.builder(SuppressWarnings.class)
								.addMember("value", "{\"rawtypes\", \"unchecked\"}")
								.build());
						builder.returns(Map.class);
						builder.addStatement("$T map = new $T($L)", Map.class,
								LinkedHashMap.class, map.size());
						map.forEach((key, value) -> builder.addStatement("map.put($L, $L)",
								BeanDefinitionPropertyValueCodeGenerator.this
										.generateCode(key, keyType),
								BeanDefinitionPropertyValueCodeGenerator.this
										.generateCode(value, valueType)));
						builder.addStatement("return map");
					});
			return CodeBlock.of("$L()", method.getName());
		}

	}


	/**
	 * {@link Delegate} for {@link BeanReference} types.
	 */
	private static class BeanReferenceDelegate implements Delegate {

		@Override
		@Nullable
		public CodeBlock generateCode(Object value, ResolvableType type) {
			if (value instanceof BeanReference beanReference) {
				return CodeBlock.of("new $T($S)", RuntimeBeanReference.class,
						beanReference.getBeanName());
			}
			return null;
		}

	}

}
