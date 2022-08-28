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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HttpRequestValues}.
 *
 * @author Rossen Stoyanchev
 */
public class HttpRequestValuesTests {

	@Test
	void defaultUri() {
		HttpRequestValues requestValues = HttpRequestValues.builder(HttpMethod.GET).build();

		assertThat(requestValues.getUri()).isNull();
		assertThat(requestValues.getUriTemplate()).isEqualTo("");
	}

	@ParameterizedTest
	@ValueSource(strings = {"POST", "PUT", "PATCH"})
	void requestParamAsFormData(String httpMethod) {

		HttpRequestValues requestValues = HttpRequestValues.builder(HttpMethod.valueOf(httpMethod))
				.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
				.addRequestParameter("param1", "1st value")
				.addRequestParameter("param2", "2nd value A", "2nd value B")
				.build();

		Object body = requestValues.getBodyValue();
		assertThat(body).isNotNull().isInstanceOf(byte[].class);
		assertThat(new String((byte[]) body, UTF_8)).isEqualTo("param1=1st+value&param2=2nd+value+A&param2=2nd+value+B");
	}

	@Test
	void requestParamAsQueryParamsInUriTemplate() {

		HttpRequestValues requestValues = HttpRequestValues.builder(HttpMethod.POST)
				.setUriTemplate("/path")
				.addRequestParameter("param1", "1st value")
				.addRequestParameter("param2", "2nd value A", "2nd value B")
				.build();

		String uriTemplate = requestValues.getUriTemplate();
		assertThat(uriTemplate).isNotNull();

		assertThat(uriTemplate)
				.isEqualTo("/path?" +
						"{queryParam0}={queryParam0[0]}&" +
						"{queryParam1}={queryParam1[0]}&" +
						"{queryParam1}={queryParam1[1]}");

		assertThat(requestValues.getUriVariables())
				.containsOnlyKeys("queryParam0", "queryParam1", "queryParam0[0]", "queryParam1[0]", "queryParam1[1]")
				.containsEntry("queryParam0", "param1")
				.containsEntry("queryParam1", "param2")
				.containsEntry("queryParam0[0]", "1st value")
				.containsEntry("queryParam1[0]", "2nd value A")
				.containsEntry("queryParam1[1]", "2nd value B");

		URI uri = UriComponentsBuilder.fromUriString(uriTemplate)
				.encode()
				.build(requestValues.getUriVariables());

		assertThat(uri.toString())
				.isEqualTo("/path?param1=1st%20value&param2=2nd%20value%20A&param2=2nd%20value%20B");
	}

	@Test
	void requestParamAsQueryParamsInUri() {

		HttpRequestValues requestValues = HttpRequestValues.builder(HttpMethod.POST)
				.setUri(URI.create("/path"))
				.addRequestParameter("param1", "1st value")
				.addRequestParameter("param2", "2nd value A", "2nd value B")
				.build();

		assertThat(requestValues.getUri().toString())
				.isEqualTo("/path?param1=1st%20value&param2=2nd%20value%20A&param2=2nd%20value%20B");
	}

}
