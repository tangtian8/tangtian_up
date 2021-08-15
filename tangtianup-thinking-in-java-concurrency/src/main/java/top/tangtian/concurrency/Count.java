package top.tangtian.concurrency;

/**
 * @author tangtian
 * @description
 * @date 2021/8/15 9:18
 */
public class Count {
    public static void main(String[] args) {
        CountAdd countAdd = new CountAdd();
        Thread thread1 = new Thread(countAdd,"count1");
        Thread thread2 = new Thread(countAdd,"count2");
        thread1.start();
        thread2.start();
    }
    static class CountAdd implements Runnable{

        private volatile int num = 0;
        @Override
        public synchronized void run() {
            for (int i = 0; i< 10000;i++){
                num++;
            }
            System.out.println(Thread.currentThread().getName() + " "+ num);
        }
    }
}
