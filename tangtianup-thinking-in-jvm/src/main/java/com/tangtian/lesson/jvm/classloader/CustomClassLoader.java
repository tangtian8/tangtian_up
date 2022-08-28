package com.tangtian.lesson.jvm.classloader;

/**
 * @author tangtian
 * @description
 * @date 2022/8/29 7:38
 */
public class CustomClassLoader extends ClassLoader{
    private String classpath;

    public CustomClassLoader(String classpath) {
        this.classpath = classpath;
    }
}
