package top.tangtian.basic;

/**
 * @author tangtian
 * @description
 * @date 2021/7/26 7:49
 */
public class BasicMain {
    public static void main(String[] args) {
        BasicThreadRunnable t2 = new BasicThreadRunnable();
        Thread thread1 = new Thread(t2,"Thread-A");
        Thread thread2 = new Thread(t2,"Thread-B");
        Thread thread3 = new Thread(t2,"Thread-C");
        Thread thread4 = new Thread(t2,"Thread-D");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
