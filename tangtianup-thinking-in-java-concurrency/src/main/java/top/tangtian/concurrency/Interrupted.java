package top.tangtian.concurrency;

import top.tangtian.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2021/8/14 16:18
 */
public class Interrupted {
    public static void main(String[] args) {
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        //让上述两个线程运行一会
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread->" + sleepThread.isInterrupted());
        System.out.println("BusyThread->" + busyThread.isInterrupted());
        SleepUtils.second(2L);
    }

    static class SleepRunner implements Runnable{
        @Override
        public void run() {
            while (true){
                SleepUtils.second(10L);
            }
        }
    }

    static class BusyRunner implements Runnable {

        @Override
        public void run() {
            while (true){

            }
        }
    }
}
