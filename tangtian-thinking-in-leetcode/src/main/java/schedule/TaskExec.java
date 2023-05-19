package schedule;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2023/4/28 8:39
 */
public class TaskExec {
    private static  String a = "1232";

    static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            return thread;
        }
    });


    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        executorService.schedule(task,4, TimeUnit.SECONDS);
        MyTask1 task1 = new MyTask1();
        executorService.schedule(task1,2, TimeUnit.SECONDS);
//        TimeUnit.SECONDS.sleep(10000);
    }

     static class MyTask implements Runnable{
        @Override
        public void run() {
            try {
                Thread t = Thread.currentThread();
                String name = t.getName();
                System.out.println("my task has been runed" + name);
            }catch (Exception e){

            }finally {
                executorService.schedule(new MyTask(),2,TimeUnit.SECONDS);
            }

        }
    }

    static class MyTask1 implements Runnable{
        @Override
        public void run() {
            try {
                Thread t = Thread.currentThread();
                String name = t.getName();
                System.out.println("my task has been runed111" + name);
            }catch (Exception e){

            }finally {
                executorService.schedule(new MyTask1(),2,TimeUnit.SECONDS);
            }

        }
    }
}
