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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.JavaSerializationHints;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.ResourceHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.codec.StringDecoder;
import org.springframework.util.MimeType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FileNativeConfigurationWriter}.
 *
 * @author Sebastien Deleuze
 */
public class FileNativeConfigurationWriterTests {

	@TempDir
	static Path tempDir;

	@Test
	void emptyConfig() {
		Path empty = tempDir.resolve("empty");
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(empty);
		generator.write(new RuntimeHints());
		assertThat(empty.toFile().listFiles()).isNull();
	}

	@Test
	void serializationConfig() throws IOException, JSONException {
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(tempDir);
		RuntimeHints hints = new RuntimeHints();
		JavaSerializationHints serializationHints = hints.javaSerialization();
		serializationHints.registerType(Integer.class);
		serializationHints.registerType(Long.class);
		generator.write(hints);
		assertEquals("""
				[
					{ "name": "java.lang.Integer" },
					{ "name": "java.lang.Long" }
				]""", "serialization-config.json");
	}

	@Test
	void proxyConfig() throws IOException, JSONException {
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(tempDir);
		RuntimeHints hints = new RuntimeHints();
		ProxyHints proxyHints = hints.proxies();
		proxyHints.registerJdkProxy(Function.class);
		proxyHints.registerJdkProxy(Function.class, Consumer.class);
		generator.write(hints);
		assertEquals("""
				[
					{ "interfaces": [ "java.util.function.Function" ] },
					{ "interfaces": [ "java.util.function.Function", "java.util.function.Consumer" ] }
				]""", "proxy-config.json");
	}

	@Test
	void reflectionConfig() throws IOException, JSONException {
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(tempDir);
		RuntimeHints hints = new RuntimeHints();
		ReflectionHints reflectionHints = hints.reflection();
		reflectionHints.registerType(StringDecoder.class, builder -> {
			builder
					.onReachableType(TypeReference.of(String.class))
					.withMembers(MemberCategory.PUBLIC_FIELDS, MemberCategory.DECLARED_FIELDS,
							MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
							MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
							MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INTROSPECT_DECLARED_METHODS,
							MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_METHODS,
							MemberCategory.PUBLIC_CLASSES, MemberCategory.DECLARED_CLASSES)
					.withField("DEFAULT_CHARSET", fieldBuilder -> {})
					.withField("defaultCharset", fieldBuilder -> {
						fieldBuilder.allowWrite(true);
						fieldBuilder.allowUnsafeAccess(true);
					})
					.withConstructor(List.of(TypeReference.of(List.class), TypeReference.of(boolean.class), TypeReference.of(MimeType.class)), constructorHint ->
							constructorHint.withMode(ExecutableMode.INTROSPECT))
					.withMethod("setDefaultCharset", List.of(TypeReference.of(Charset.class)), ctorBuilder -> {})
					.withMethod("getDefaultCharset", Collections.emptyList(), constructorHint ->
							constructorHint.withMode(ExecutableMode.INTROSPECT));
		});
		generator.write(hints);
		assertEquals("""
				[
					{
						"name": "org.springframework.core.codec.StringDecoder",
						"condition": { "typeReachable": "java.lang.String" },
						"allPublicFields": true,
						"allDeclaredFields": true,
						"queryAllPublicConstructors": true,
						"queryAllDeclaredConstructors": true,
						"allPublicConstructors": true,
						"allDeclaredConstructors": true,
						"queryAllPublicMethods": true,
						"queryAllDeclaredMethods": true,
						"allPublicMethods": true,
						"allDeclaredMethods": true,
						"allPublicClasses": true,
						"allDeclaredClasses": true,
						"fields": [
							{ "name": "DEFAULT_CHARSET" },
							{ "name": "defaultCharset", "allowWrite": true, "allowUnsafeAccess": true }
						],
						"methods": [
							{ "name": "setDefaultCharset", "parameterTypes": [ "java.nio.charset.Charset" ] }
						],
						"queriedMethods":  [
							{ "name": "<init>", "parameterTypes": [ "java.util.List", "boolean", "org.springframework.util.MimeType" ] },
							{ "name": "getDefaultCharset", "parameterTypes": [ ] }
						]
					}
				]""", "reflect-config.json");
	}

	@Test
	void resourceConfig() throws IOException, JSONException {
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(tempDir);
		RuntimeHints hints = new RuntimeHints();
		ResourceHints resourceHints = hints.resources();
		resourceHints.registerPattern("com/example/test.properties");
		resourceHints.registerPattern("com/example/another.properties");
		generator.write(hints);
		assertEquals("""
				{
					"resources": {
						"includes": [
							{"pattern": "\\\\Qcom/example/test.properties\\\\E"},
							{"pattern": "\\\\Qcom/example/another.properties\\\\E"}
						]
					}
				}""", "resource-config.json");
	}

	@Test
	void namespace() {
		String groupId = "foo.bar";
		String artifactId = "baz";
		String filename = "resource-config.json";
		FileNativeConfigurationWriter generator = new FileNativeConfigurationWriter(tempDir, groupId, artifactId);
		RuntimeHints hints = new RuntimeHints();
		ResourceHints resourceHints = hints.resources();
		resourceHints.registerPattern("com/example/test.properties");
		generator.write(hints);
		Path jsonFile = tempDir.resolve("META-INF").resolve("native-image").resolve(groupId).resolve(artifactId).resolve(filename);
		assertThat(jsonFile.toFile().exists()).isTrue();
	}

	private void assertEquals(String expectedString, String filename) throws IOException, JSONException {
		Path jsonFile = tempDir.resolve("META-INF").resolve("native-image").resolve(filename);
		String content = new String(Files.readAllBytes(jsonFile));
		JSONAssert.assertEquals(expectedString, content, JSONCompareMode.NON_EXTENSIBLE);
	}

}
