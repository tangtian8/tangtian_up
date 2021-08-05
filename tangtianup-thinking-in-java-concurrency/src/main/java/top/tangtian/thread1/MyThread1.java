package top.tangtian.thread1;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 7:39
 */
public class MyThread1 implements Runnable{
    private int count = 5;

    @Override
    public void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + "计算 count = " + count );
    }
}
