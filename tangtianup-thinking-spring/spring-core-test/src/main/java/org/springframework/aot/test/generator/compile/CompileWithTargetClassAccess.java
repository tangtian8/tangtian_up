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

package org.springframework.aot.test.generator.compile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Annotation that can be used on tests that need a {@link TestCompiler} with
 * non-public access to a target class. Allows the compiler to use
 * {@link MethodHandles#privateLookupIn} to {@link Lookup#defineClass define the
 * class} without polluting the test {@link ClassLoader}.
 *
 * @author Phillip Webb
 * @since 6.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@ExtendWith(CompileWithTargetClassAccessExtension.class)
public @interface CompileWithTargetClassAccess {

	/**
	 * The target class names.
	 * @return the class name
	 */
	String[] classNames() default {};

	/**
	 * The target classes.
	 * @return the classes
	 */
	Class<?>[] classes() default {};

}
