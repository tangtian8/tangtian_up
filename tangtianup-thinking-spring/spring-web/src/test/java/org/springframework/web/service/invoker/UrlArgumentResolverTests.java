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

package org.springframework.web.service.invoker;

import java.net.URI;

import org.junit.jupiter.api.Test;

import org.springframework.web.service.annotation.GetExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for {@link UrlArgumentResolver}.
 *
 * @author Rossen Stoyanchev
 */
public class UrlArgumentResolverTests {

	private final TestHttpClientAdapter client = new TestHttpClientAdapter();

	private final Service service = HttpServiceProxyFactory.builder(this.client).build().createClient(Service.class);


	@Test
	void url() {
		URI dynamicUrl = URI.create("dynamic-path");
		this.service.execute(dynamicUrl);

		assertThat(getRequestValues().getUri()).isEqualTo(dynamicUrl);
		assertThat(getRequestValues().getUriTemplate()).isNull();
	}

	@Test
	void notUrl() {
		assertThatIllegalStateException()
				.isThrownBy(() -> this.service.executeNotUri("test"))
				.withMessage("Could not resolve parameter [0] in " +
						"public abstract void org.springframework.web.service.invoker." +
						"UrlArgumentResolverTests$Service.executeNotUri(java.lang.String): " +
						"No suitable resolver");
	}

	@Test
	void ignoreNull() {
		this.service.execute(null);
		assertThat(getRequestValues().getUri()).isNull();
	}

	private HttpRequestValues getRequestValues() {
		return this.client.getRequestValues();
	}


	private interface Service {

		@GetExchange("/path")
		void execute(URI uri);

		@GetExchange
		void executeNotUri(String other);
	}

}
