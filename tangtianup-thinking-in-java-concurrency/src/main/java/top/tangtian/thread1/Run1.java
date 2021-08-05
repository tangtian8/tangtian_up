package top.tangtian.thread1;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 7:44
 */
public class Run1 {
    public static void main(String[] args) {
        MyThread1 run = new MyThread1();
        Thread a = new Thread(run,"A");
        Thread b = new Thread(run,"B");
        Thread c = new Thread(run,"C");
        Thread d = new Thread(run,"D");
        Thread e = new Thread(run,"E");
        a.start();
        b.start();
        c.start();
        d.start();
        e.start();
    }
}
