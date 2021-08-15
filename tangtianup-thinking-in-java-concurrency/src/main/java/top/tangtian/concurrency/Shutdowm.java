package top.tangtian.concurrency;


import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description 通过标识位进行中断
 * @date 2021/8/14 16:37
 */
public class Shutdowm {

    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one,"CountThread");
        countThread.start();
        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();
        Runner two = new Runner();
        countThread = new Thread(two,"CountThread");
        countThread.start();
        TimeUnit.SECONDS.sleep(1);
        two.cancel();
    }

    private static class Runner implements  Runnable{
        private long i;
        private volatile boolean on = true;
        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()){
                i++;
            }
            System.out.println("Count i =" + i);
        }
        public void cancel(){
            on = false;
        }
    }
}
