package jdkdynamic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tangtian
 * @description
 * @date 2022/11/18 7:34
 */
public class Client {
    public static void main(String[] args) {
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//        jdkdynamic.Subject subject = new jdkdynamic.JDKDynamicProxy(new jdkdynamic.RealSubject()).getProxy();
//        subject.doOtherSomething();

        Subject rs=new RealSubject();//这里指定被代理类
        InvocationHandler ds=new JDKDynamicProxy(rs);
        Class<?> cls=rs.getClass();

        //以下是一次性生成代理
//Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)做了以下几件事.
//        （1）根据参数loader和interfaces调用方法 getProxyClass(loader, interfaces)创建代理类$Proxy0.$Proxy0类 实现了interfaces的接口,并继承了Proxy类.
//        （2）实例化$Proxy0并在构造方法中把DynamicSubject传过去,接着$Proxy0调用父类Proxy的构造器,为h赋值,如下：
        Subject subject=(Subject) Proxy.newProxyInstance(
                cls.getClassLoader(),cls.getInterfaces(), ds);

        //这里可以通过运行结果证明subject是Proxy的一个实例，这个实例实现了Subject接口
        System.out.println(subject instanceof Proxy);

        //这里可以看出subject的Class类是$Proxy0,这个$Proxy0类继承了Proxy，实现了Subject接口
        System.out.println("subject的Class类是："+subject.getClass().toString());

        System.out.print("subject中的属性有：");

        Field[] field=subject.getClass().getDeclaredFields();
        for(Field f:field){
            System.out.print(f.getName()+", ");
        }

        System.out.print("\n"+"subject中的方法有：");

        Method[] method=subject.getClass().getDeclaredMethods();

        for(Method m:method){
            System.out.print(m.getName()+", ");
        }

        System.out.println("\n"+"subject的父类是："+subject.getClass().getSuperclass());

        System.out.print("\n"+"subject实现的接口是：");

        Class<?>[] interfaces=subject.getClass().getInterfaces();

        for(Class<?> i:interfaces){
            System.out.print(i.getName()+", ");
        }

        System.out.println("\n\n"+"运行结果为：");
        subject.doSomething();
    }
}
