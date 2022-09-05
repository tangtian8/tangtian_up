package com.tangtian.lesson.jvm.memory;

/**
 * @author tangtian
 * @description
 * @date 2022/9/3 17:05
 */
public class ObjectCreate {
    //静态变量
    public  static  int staicVariabl=1;
    //成员变量
    public   int  objVariabl;

    //静态初始代码块
    static {
        staicVariabl=2;
    }

    //对象初始化代码块
    {
        objVariabl=88;
    }

    //构造方法
    public ObjectCreate() {
        objVariabl=99;
    }

    public static void main(String[] args) {
        ObjectCreate objectCreate =new ObjectCreate();
    }
}
