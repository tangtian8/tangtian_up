package com.tangtian.lesson.jvm.memory;

/**
 * @author tangtian
 * @description
 * @date 2022/9/3 9:02
 */
public class StackDemo {
    public static void main(String[] args) {
        System.out.println("main 方法start");

        StackDemo stackDemo = new StackDemo();
        stackDemo.methodA();

        System.out.println("main 方法end");
    }

    public void methodA(){
        System.out.println("methodA start");
        methodB();
        System.out.println("methodA end");
    }

    public void methodB(){
        System.out.println("methodB start");
        methodC();
        System.out.println("methodB end");
    }

    public void methodC(){
        System.out.println("methodC start");
        System.out.println("methodC end");
    }
}
