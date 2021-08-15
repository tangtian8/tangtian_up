package top.tangtian.concurrency;

import top.tangtian.SleepUtils;

/**
 * @author tangtian
 * @description
 * @date 2021/8/14 14:29
 */
public class Deamon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(),"DaemonRunner");
        //thread.setDaemon(true);//此方法设置为Daemon方法,并且在启动线程之前设置
        thread.start();
    }
    static class DaemonRunner implements Runnable{
        @Override
        public void run() {
            try {
                SleepUtils.second(10L);
            }finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
