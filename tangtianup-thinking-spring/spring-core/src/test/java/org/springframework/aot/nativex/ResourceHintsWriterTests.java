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

package org.springframework.aot.nativex;

import java.io.StringWriter;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.springframework.aot.hint.ResourceHints;

/**
 * Tests for {@link ResourceHintsWriter}.
 *
 * @author Sebastien Deleuze
 */
public class ResourceHintsWriterTests {

	@Test
	void empty() throws JSONException {
		ResourceHints hints = new ResourceHints();
		assertEquals("{}", hints);
	}

	@Test
	void registerExactMatch() throws JSONException {
		ResourceHints hints = new ResourceHints();
		hints.registerPattern("com/example/test.properties");
		hints.registerPattern("com/example/another.properties");
		assertEquals("""
				{
					"resources": {
						"includes": [
							{ "pattern": "\\\\Qcom/example/test.properties\\\\E"},
							{ "pattern": "\\\\Qcom/example/another.properties\\\\E"}
						]
					}
				}""", hints);
	}

	@Test
	void registerPattern() throws JSONException {
		ResourceHints hints = new ResourceHints();
		hints.registerPattern("com/example/*.properties");
		assertEquals("""
				{
					"resources": {
						"includes": [
							{ "pattern": "\\\\Qcom/example/\\\\E.*\\\\Q.properties\\\\E"}
						]
					}
				}""", hints);
	}

	@Test
	void registerPatternWithIncludesAndExcludes() throws JSONException {
		ResourceHints hints = new ResourceHints();
		hints.registerPattern("com/example/*.properties", hint -> hint.excludes("com/example/to-ignore.properties"));
		hints.registerPattern("org/example/*.properties", hint -> hint.excludes("org/example/to-ignore.properties"));
		assertEquals("""
				{
					"resources": {
						"includes": [
							{ "pattern": "\\\\Qcom/example/\\\\E.*\\\\Q.properties\\\\E"},
							{ "pattern": "\\\\Qorg/example/\\\\E.*\\\\Q.properties\\\\E"}
						],
						"excludes": [
							{ "pattern": "\\\\Qcom/example/to-ignore.properties\\\\E"},
							{ "pattern": "\\\\Qorg/example/to-ignore.properties\\\\E"}
						]
					}
				}""", hints);
	}

	@Test
	void registerType() throws JSONException {
		ResourceHints hints = new ResourceHints();
		hints.registerType(String.class);
		assertEquals("""
				{
					"resources": {
						"includes": [
							{ "pattern": "\\\\Qjava/lang/String.class\\\\E"}
						]
					}
				}""", hints);
	}

	@Test
	void registerResourceBundle() throws JSONException {
		ResourceHints hints = new ResourceHints();
		hints.registerResourceBundle("com.example.message");
		hints.registerResourceBundle("com.example.message2");
		assertEquals("""
				{
					"bundles": [
						{ "name": "com.example.message"},
						{ "name": "com.example.message2"}
					]
				}""", hints);
	}

	private void assertEquals(String expectedString, ResourceHints hints) throws JSONException {
		StringWriter out = new StringWriter();
		BasicJsonWriter writer = new BasicJsonWriter(out, "\t");
		ResourceHintsWriter.INSTANCE.write(writer, hints);
		JSONAssert.assertEquals(expectedString, out.toString(), JSONCompareMode.NON_EXTENSIBLE);
	}

}
