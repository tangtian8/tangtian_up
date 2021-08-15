package top.tangtian.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2021/8/12 8:28
 */
public class Priority {
    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            int priority = i < 5? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job,"thread" + i);
            thread.setPriority(priority);
            thread.start();
        }
        notStart = false;
        try {
            TimeUnit.SECONDS.sleep(10);
            notEnd = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Job job : jobs){
            System.out.println("Job Priority:" + job.priority + ", count : " + job.jobCount);
        }
    }
    static class Job implements Runnable{
        private int priority;
        private long jobCount;

        public Job(int priority){
            this.priority = priority;
        }

        @Override
        public void run() {
            while (notStart){
                Thread.yield();
            }
            while (notEnd){
                Thread.yield();
                jobCount++;
            }
        }
    }
}
