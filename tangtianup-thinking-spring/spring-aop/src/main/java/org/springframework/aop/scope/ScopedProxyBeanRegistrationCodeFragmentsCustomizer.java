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

package org.springframework.aop.scope;

import java.lang.reflect.Executable;
import java.util.function.Predicate;

import javax.lang.model.element.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aot.generate.GeneratedMethod;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.beans.factory.aot.BeanRegistrationCode;
import org.springframework.beans.factory.aot.BeanRegistrationCodeFragments;
import org.springframework.beans.factory.aot.BeanRegistrationCodeFragmentsCustomizer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.javapoet.CodeBlock;
import org.springframework.lang.Nullable;

/**
 * {@link BeanRegistrationCodeFragmentsCustomizer} for
 * {@link ScopedProxyFactoryBean}.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 6.0
 */
class ScopedProxyBeanRegistrationCodeFragmentsCustomizer
		implements BeanRegistrationCodeFragmentsCustomizer {

	private static final Log logger = LogFactory
			.getLog(ScopedProxyBeanRegistrationCodeFragmentsCustomizer.class);


	@Override
	public BeanRegistrationCodeFragments customizeBeanRegistrationCodeFragments(
			RegisteredBean registeredBean, BeanRegistrationCodeFragments codeFragments) {

		Class<?> beanType = registeredBean.getBeanType().toClass();
		if (!beanType.equals(ScopedProxyFactoryBean.class)) {
			return codeFragments;
		}
		String targetBeanName = getTargetBeanName(
				registeredBean.getMergedBeanDefinition());
		BeanDefinition targetBeanDefinition = getTargetBeanDefinition(
				registeredBean.getBeanFactory(), targetBeanName);
		if (targetBeanDefinition == null) {
			logger.warn("Could not handle " + ScopedProxyFactoryBean.class.getSimpleName()
					+ ": no target bean definition found with name " + targetBeanName);
			return codeFragments;
		}
		return new ScopedProxyBeanRegistrationCodeFragments(codeFragments, registeredBean,
				targetBeanName, targetBeanDefinition);
	}

	@Nullable
	private String getTargetBeanName(BeanDefinition beanDefinition) {
		Object value = beanDefinition.getPropertyValues().get("targetBeanName");
		return (value instanceof String) ? (String) value : null;
	}

	@Nullable
	private BeanDefinition getTargetBeanDefinition(ConfigurableBeanFactory beanFactory,
			@Nullable String targetBeanName) {

		if (targetBeanName != null && beanFactory.containsBean(targetBeanName)) {
			return beanFactory.getMergedBeanDefinition(targetBeanName);
		}
		return null;
	}


	private static class ScopedProxyBeanRegistrationCodeFragments
			extends BeanRegistrationCodeFragments {

		private static final String REGISTERED_BEAN_PARAMETER_NAME = "registeredBean";


		private final RegisteredBean registeredBean;

		private final String targetBeanName;

		private final BeanDefinition targetBeanDefinition;


		ScopedProxyBeanRegistrationCodeFragments(
				BeanRegistrationCodeFragments codeGenerator,
				RegisteredBean registeredBean, String targetBeanName,
				BeanDefinition targetBeanDefinition) {

			super(codeGenerator);
			this.registeredBean = registeredBean;
			this.targetBeanName = targetBeanName;
			this.targetBeanDefinition = targetBeanDefinition;
		}


		@Override
		public Class<?> getTarget(RegisteredBean registeredBean,
				Executable constructorOrFactoryMethod) {
			return this.targetBeanDefinition.getResolvableType().toClass();
		}

		@Override
		public CodeBlock generateNewBeanDefinitionCode(
				GenerationContext generationContext, ResolvableType beanType,
				BeanRegistrationCode beanRegistrationCode) {

			return super.generateNewBeanDefinitionCode(generationContext,
					this.targetBeanDefinition.getResolvableType(), beanRegistrationCode);
		}

		@Override
		public CodeBlock generateSetBeanDefinitionPropertiesCode(
				GenerationContext generationContext,
				BeanRegistrationCode beanRegistrationCode,
				RootBeanDefinition beanDefinition, Predicate<String> attributeFilter) {

			RootBeanDefinition processedBeanDefinition = new RootBeanDefinition(
					beanDefinition);
			processedBeanDefinition
					.setTargetType(this.targetBeanDefinition.getResolvableType());
			processedBeanDefinition.getPropertyValues()
					.removePropertyValue("targetBeanName");
			return super.generateSetBeanDefinitionPropertiesCode(generationContext,
					beanRegistrationCode, processedBeanDefinition, attributeFilter);
		}

		@Override
		public CodeBlock generateInstanceSupplierCode(GenerationContext generationContext,
				BeanRegistrationCode beanRegistrationCode,
				Executable constructorOrFactoryMethod,
				boolean allowDirectSupplierShortcut) {

			GeneratedMethod method = beanRegistrationCode.getMethodGenerator()
					.generateMethod("get", "scopedProxyInstance").using(builder -> {
						Class<?> beanClass = this.targetBeanDefinition.getResolvableType()
								.toClass();
						builder.addJavadoc(
								"Create the scoped proxy bean instance for '$L'.",
								this.registeredBean.getBeanName());
						builder.addModifiers(Modifier.PRIVATE, Modifier.STATIC);
						builder.returns(beanClass);
						builder.addParameter(RegisteredBean.class,
								REGISTERED_BEAN_PARAMETER_NAME);
						builder.addStatement("$T factory = new $T()",
								ScopedProxyFactoryBean.class,
								ScopedProxyFactoryBean.class);
						builder.addStatement("factory.setTargetBeanName($S)",
								this.targetBeanName);
						builder.addStatement(
								"factory.setBeanFactory($L.getBeanFactory())",
								REGISTERED_BEAN_PARAMETER_NAME);
						builder.addStatement("return ($T) factory.getObject()",
								beanClass);
					});
			return CodeBlock.of("$T.of($T::$L)", InstanceSupplier.class,
					beanRegistrationCode.getClassName(), method.getName());
		}

	}

}
