package top.tangtian.concurrency;

import top.tangtian.SleepUtils;

/**
 * @author tangtian
 * @description
 * @date 2021/8/14 11:11
 */
public class ThreadStatus {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();
        new Thread(new Waiting(),"WaitingThread").start();
        //使用两个blocked 线程,一个获取锁成功,另外一个阻塞
        new Thread(new Blocked(),"BlockedThread-1").start();
        new Thread(new Blocked(),"BlockedThread-2").start();
    }
    //该线程不断进行睡眠
    static class TimeWaiting implements  Runnable{
        @Override
        public void run() {
            while (true){
                SleepUtils.second(100L);
            }
        }
    }

    //该线程在Waiting.class 实例上等待
    static class Waiting implements Runnable{
        @Override
        public void run() {
            while (true){
                synchronized (Waiting.class){
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //该线程在Blocked.class实力上加锁后,不释放该锁
    static class Blocked implements Runnable{
        @Override
        public void run() {
            synchronized (Blocked.class){
                while (true){
                    SleepUtils.second(100L);
                }
            }
        }
    }
}
