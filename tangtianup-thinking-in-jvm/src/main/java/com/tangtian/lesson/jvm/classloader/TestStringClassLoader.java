package com.tangtian.lesson.jvm.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tangtian
 * @description
 * @date 2022/8/29 7:48
 */
public class TestStringClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //自定义类加载器的加载路径
        CustomClassLoader hClassLoader=new CustomClassLoader("D:\\hero-workspace\\hero_example-v2.0\\day03-jvm\\target\\classes");
        //包名+类名
        Class c=hClassLoader.loadClass("java.lang.String");

        if(c!=null){
            Object obj=c.newInstance();
            Method method=c.getMethod("toString", null);
            method.invoke(obj, null);
            System.out.println(c.getClassLoader().toString());
        }
    }

}
