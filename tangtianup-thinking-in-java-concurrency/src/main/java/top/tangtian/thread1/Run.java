package top.tangtian.thread1;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 7:44
 */
public class Run {
    public static void main(String[] args) {
        MyThread a = new MyThread("A");
        MyThread b = new MyThread("B");
        MyThread c = new MyThread("C");
        a.start();
        b.start();
        c.start();
     }
}
