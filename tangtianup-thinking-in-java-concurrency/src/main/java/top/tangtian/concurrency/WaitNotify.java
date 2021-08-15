package top.tangtian.concurrency;

import top.tangtian.SleepUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2021/8/14 18:49
 */
public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(),"WaitingThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1L);
        Thread notifyThread = new Thread(new Notify(),"NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable{
        @Override
        public void run() {
            //加锁，拥有lock的Monitor
            synchronized (lock){
                //当条件不满足时候,继续wait,同时释放lock的锁
                System.out.println(Thread.currentThread() + "flag is true. wait@" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    lock.wait();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            //条件满足时候完成工作
            System.out.println(Thread.currentThread() + "flag is false, running@" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
    }


    static class Notify implements Runnable{
        @Override
        public void run() {
        //加锁，拥有lock的Monitor
           synchronized (lock){
               //获取lock的锁,然后进行通知，通知时不会释放lock的锁。直到当前线程释放了lock后，WaitThread才能从wait方法中返回
               System.out.println(Thread.currentThread() + "hold Lock. notify@" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
               lock.notifyAll();
               flag = false;
               SleepUtils.second(5L);
           }
           synchronized (lock){
               System.out.println(Thread.currentThread() + "hold Lock again. notify@" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
               SleepUtils.second(5L);
           }
        }
    }
}
