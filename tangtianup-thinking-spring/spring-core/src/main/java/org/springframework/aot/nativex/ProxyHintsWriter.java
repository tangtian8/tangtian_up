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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.aot.hint.JdkProxyHint;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.TypeReference;

/**
 * Write {@link JdkProxyHint}s contained in a {@link ProxyHints} to the JSON
 * output expected by the GraalVM {@code native-image} compiler, typically named
 * {@code proxy-config.json}.
 *
 * @author Sebastien Deleuze
 * @author Stephane Nicoll
 * @since 6.0
 * @see <a href="https://www.graalvm.org/22.0/reference-manual/native-image/DynamicProxy/">Dynamic Proxy in Native Image</a>
 * @see <a href="https://www.graalvm.org/22.0/reference-manual/native-image/BuildConfiguration/">Native Image Build Configuration</a>
 */
class ProxyHintsWriter {

	public static final ProxyHintsWriter INSTANCE = new ProxyHintsWriter();

	public void write(BasicJsonWriter writer, ProxyHints hints) {
		writer.writeArray(hints.jdkProxies().map(this::toAttributes).toList());
	}

	private Map<String, Object> toAttributes(JdkProxyHint hint) {
		Map<String, Object> attributes = new LinkedHashMap<>();
		attributes.put("interfaces", hint.getProxiedInterfaces().stream()
				.map(TypeReference::getCanonicalName).toList());
		return attributes;
	}

}
