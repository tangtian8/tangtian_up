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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.core.log.LogMessage;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * Factory used to create a {@link BeanDefinitionMethodGenerator} instance for a
 * {@link RegisteredBean}.
 *
 * @author Phillip Webb
 * @since 6.0
 * @see BeanDefinitionMethodGenerator
 * @see #getBeanDefinitionMethodGenerator(RegisteredBean, String)
 */
class BeanDefinitionMethodGeneratorFactory {

	private static final Log logger = LogFactory
			.getLog(BeanDefinitionMethodGeneratorFactory.class);


	private final List<BeanRegistrationAotProcessor> aotProcessors;

	private final List<BeanRegistrationExcludeFilter> excludeFilters;

	private final List<BeanRegistrationCodeFragmentsCustomizer> codeGenerationCustomizers;


	/**
	 * Create a new {@link BeanDefinitionMethodGeneratorFactory} backed by the
	 * given {@link ConfigurableListableBeanFactory}.
	 * @param beanFactory the bean factory use
	 */
	BeanDefinitionMethodGeneratorFactory(ConfigurableListableBeanFactory beanFactory) {
		this(new AotFactoriesLoader(beanFactory));
	}

	/**
	 * Create a new {@link BeanDefinitionMethodGeneratorFactory} backed by the
	 * given {@link AotFactoriesLoader}.
	 * @param loader the AOT factory loader to use
	 */
	BeanDefinitionMethodGeneratorFactory(AotFactoriesLoader loader) {
		this.aotProcessors = loader.load(BeanRegistrationAotProcessor.class);
		this.excludeFilters = loader.load(BeanRegistrationExcludeFilter.class);
		this.codeGenerationCustomizers = loader
				.load(BeanRegistrationCodeFragmentsCustomizer.class);
	}


	/**
	 * Return a {@link BeanDefinitionMethodGenerator} for the given
	 * {@link RegisteredBean} or {@code null} if the registered bean is excluded
	 * by a {@link BeanRegistrationExcludeFilter}. The resulting
	 * {@link BeanDefinitionMethodGenerator} will include all
	 * {@link BeanRegistrationAotProcessor} provided contributions.
	 * @param registeredBean the registered bean
	 * @return a new {@link BeanDefinitionMethodGenerator} instance or
	 * {@code null}
	 */
	@Nullable
	BeanDefinitionMethodGenerator getBeanDefinitionMethodGenerator(
			RegisteredBean registeredBean, @Nullable String innerBeanPropertyName) {

		if (isExcluded(registeredBean)) {
			return null;
		}
		List<BeanRegistrationAotContribution> contributions = getAotContributions(
				registeredBean);
		return new BeanDefinitionMethodGenerator(this, registeredBean,
				innerBeanPropertyName, contributions, this.codeGenerationCustomizers);
	}

	private boolean isExcluded(RegisteredBean registeredBean) {
		if (isImplicitlyExcluded(registeredBean)) {
			return true;
		}
		for (BeanRegistrationExcludeFilter excludeFilter : this.excludeFilters) {
			if (excludeFilter.isExcluded(registeredBean)) {
				logger.trace(LogMessage.format(
						"Excluding registered bean '%s' from bean factory %s due to %s",
						registeredBean.getBeanName(),
						ObjectUtils.identityToString(registeredBean.getBeanFactory()),
						excludeFilter.getClass().getName()));
				return true;
			}
		}
		return false;
	}

	private boolean isImplicitlyExcluded(RegisteredBean registeredBean) {
		Class<?> beanClass = registeredBean.getBeanClass();
		return BeanFactoryInitializationAotProcessor.class.isAssignableFrom(beanClass)
				|| BeanRegistrationAotProcessor.class.isAssignableFrom(beanClass);
	}

	private List<BeanRegistrationAotContribution> getAotContributions(
			RegisteredBean registeredBean) {

		String beanName = registeredBean.getBeanName();
		List<BeanRegistrationAotContribution> contributions = new ArrayList<>();
		for (BeanRegistrationAotProcessor aotProcessor : this.aotProcessors) {
			BeanRegistrationAotContribution contribution = aotProcessor
					.processAheadOfTime(registeredBean);
			if (contribution != null) {
				logger.trace(LogMessage.format(
						"Adding bean registration AOT contribution %S from %S to '%S'",
						contribution.getClass().getName(),
						aotProcessor.getClass().getName(), beanName));
				contributions.add(contribution);
			}
		}
		return contributions;
	}

}
