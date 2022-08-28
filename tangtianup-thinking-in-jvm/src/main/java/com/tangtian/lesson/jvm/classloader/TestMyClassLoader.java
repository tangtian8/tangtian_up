package com.tangtian.lesson.jvm.classloader;

import java.lang.reflect.Method;

/**
 * @author tangtian
 * @description
 * @date 2022/8/29 7:45
 */
public class TestMyClassLoader {
    public static void main(String []args) throws Exception{
        //自定义类加载器的加载路径
        TangTianClassLoad hClassLoader=new TangTianClassLoad("D:\\lib");
        //包名+类名
        Class c=hClassLoader.findClass("com.hero.jvm.classloader.Test");

        if(c!=null){
            Object obj=c.newInstance();
            Method method=c.getMethod("say", null);
            method.invoke(obj, null);
            System.out.println(c.getClassLoader().toString());
        }
    }
}
