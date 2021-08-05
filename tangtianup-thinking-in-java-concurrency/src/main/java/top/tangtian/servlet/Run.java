package top.tangtian.servlet;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 8:30
 */
public class Run {


    public static void main(String[] args) {
        Thread thread = new Thread(new Runable1());
        Thread thread2 = new Thread(new Runable2());
        thread.start();
        thread2.start();
    }
}
