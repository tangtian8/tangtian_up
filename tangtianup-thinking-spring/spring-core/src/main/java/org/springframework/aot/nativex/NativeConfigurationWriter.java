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

import java.util.function.Consumer;

import org.springframework.aot.hint.JavaSerializationHints;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.ResourceHints;
import org.springframework.aot.hint.RuntimeHints;

/**
 * Write {@link RuntimeHints} as GraalVM native configuration.
 *
 * @author Sebastien Deleuze
 * @author Stephane Nicoll
 * @since 6.0
 * @see <a href="https://www.graalvm.org/22.0/reference-manual/native-image/BuildConfiguration/">Native Image Build Configuration</a>
 */
public abstract class NativeConfigurationWriter {

	/**
	 * Write the GraalVM native configuration from the provided hints.
	 * @param hints the hints to handle
	 */
	public void write(RuntimeHints hints) {
		if (hints.javaSerialization().types().findAny().isPresent()) {
			writeJavaSerializationHints(hints.javaSerialization());
		}
		if (hints.proxies().jdkProxies().findAny().isPresent()) {
			writeProxyHints(hints.proxies());
		}
		if (hints.reflection().typeHints().findAny().isPresent()) {
			writeReflectionHints(hints.reflection());
		}
		if (hints.resources().resourcePatterns().findAny().isPresent() ||
				hints.resources().resourceBundles().findAny().isPresent()) {
			writeResourceHints(hints.resources());
		}
	}

	/**
	 * Write the specified GraalVM native configuration file, using the
	 * provided {@link BasicJsonWriter}.
	 * @param fileName the name of the file
	 * @param writer a consumer for the writer to use
	 */
	protected abstract void writeTo(String fileName, Consumer<BasicJsonWriter> writer);

	private void writeJavaSerializationHints(JavaSerializationHints hints) {
		writeTo("serialization-config.json", writer ->
				JavaSerializationHintsWriter.INSTANCE.write(writer, hints));
	}

	private void writeProxyHints(ProxyHints hints) {
		writeTo("proxy-config.json", writer ->
				ProxyHintsWriter.INSTANCE.write(writer, hints));
	}

	private void writeReflectionHints(ReflectionHints hints) {
		writeTo("reflect-config.json", writer ->
				ReflectionHintsWriter.INSTANCE.write(writer, hints));
	}

	private void writeResourceHints(ResourceHints hints) {
		writeTo("resource-config.json", writer ->
				ResourceHintsWriter.INSTANCE.write(writer, hints));
	}

}
