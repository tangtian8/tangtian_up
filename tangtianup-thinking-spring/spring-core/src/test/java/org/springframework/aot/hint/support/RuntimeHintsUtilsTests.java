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

package org.springframework.aot.hint.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import org.springframework.aot.hint.JdkProxyHint;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.TypeHint;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.SynthesizedAnnotation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RuntimeHintsUtils}.
 *
 * @author Stephane Nicoll
 */
class RuntimeHintsUtilsTests {

	private final RuntimeHints hints = new RuntimeHints();

	@Test
	void registerAnnotation() {
		RuntimeHintsUtils.registerAnnotation(this.hints, MergedAnnotations
				.from(SampleInvokerClass.class).get(SampleInvoker.class));
		assertThat(this.hints.reflection().typeHints()).singleElement()
				.satisfies(annotationHint(SampleInvoker.class));
		assertThat(this.hints.proxies().jdkProxies()).isEmpty();
	}

	@Test
	void registerAnnotationProxyRegistersJdkProxy() {
		RuntimeHintsUtils.registerAnnotation(this.hints, MergedAnnotations
				.from(RetryInvokerClass.class).get(RetryInvoker.class));
		assertThat(this.hints.reflection().typeHints()).singleElement()
				.satisfies(annotationHint(RetryInvoker.class));
		assertThat(this.hints.proxies().jdkProxies()).singleElement()
				.satisfies(annotationProxy(RetryInvoker.class));
	}

	@Test
	void registerAnnotationWhereUsedAsAMetaAnnotationRegistersHierarchy() {
		RuntimeHintsUtils.registerAnnotation(this.hints, MergedAnnotations
				.from(RetryWithEnabledFlagInvokerClass.class).get(SampleInvoker.class));
		ReflectionHints reflection = this.hints.reflection();
		assertThat(reflection.typeHints())
				.anySatisfy(annotationHint(SampleInvoker.class))
				.anySatisfy(annotationHint(RetryInvoker.class))
				.anySatisfy(annotationHint(RetryWithEnabledFlagInvoker.class))
				.hasSize(3);
		assertThat(this.hints.proxies().jdkProxies()).singleElement()
				.satisfies(annotationProxy(SampleInvoker.class));
	}

	private Consumer<TypeHint> annotationHint(Class<?> type) {
		return typeHint -> {
			assertThat(typeHint.getType()).isEqualTo(TypeReference.of(type));
			assertThat(typeHint.constructors()).isEmpty();
			assertThat(typeHint.fields()).isEmpty();
			assertThat(typeHint.methods()).isEmpty();
			assertThat(typeHint.getMemberCategories()).containsOnly(MemberCategory.INVOKE_PUBLIC_METHODS);
		};
	}

	private Consumer<JdkProxyHint> annotationProxy(Class<?> type) {
		return jdkProxyHint -> assertThat(jdkProxyHint.getProxiedInterfaces())
				.containsExactly(TypeReference.of(type),
						TypeReference.of(SynthesizedAnnotation.class));
	}


	@SampleInvoker
	static class SampleInvokerClass {

	}

	@RetryInvoker
	static class RetryInvokerClass {

	}

	@RetryWithEnabledFlagInvoker
	static class RetryWithEnabledFlagInvokerClass {

	}


	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface SampleInvoker {

		int retries() default 0;

	}

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@SampleInvoker
	@interface RetryInvoker {

		@AliasFor(attribute = "retries", annotation = SampleInvoker.class)
		int value() default 1;

	}

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@RetryInvoker
	@interface RetryWithEnabledFlagInvoker {

		@AliasFor(attribute = "value", annotation = RetryInvoker.class)
		int value() default 5;

		boolean enabled() default true;

	}

}
