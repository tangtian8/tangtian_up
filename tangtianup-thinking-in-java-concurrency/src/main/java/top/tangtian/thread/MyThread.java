package top.tangtian.thread;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 6:38
 */
public class MyThread extends Thread{
    @Override
    public void run() {
        super.run();
        System.out.println("MyThread");
    }
}
