package jdkdynamic;

/**
 * @author tangtian
 * @description
 * @date 2022/11/18 7:32
 */
public class RealSubject implements Subject{
    @Override
    public void doSomething() {
        System.out.println("this is my first Invocation");
    }

    @Override
    public void doOtherSomething() {
        System.out.println("this is my second Invocation");
    }
}
