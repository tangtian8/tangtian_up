package top.tangtian.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tangtian
 * @description这种场景通常用于保存线程不安全的工具类，典型的需要使用的类就是 SimpleDateFormat。
 *
 * 在这种情况下，每个Thread内都有自己的实例副本，且该副本只能由当前Thread访问到并使用，
 * 相当于每个线程内部的本地变量，这也是ThreadLocal命名的含义。因为每个线程独享副本，而不是公用的，所以不存在多线程间共享的问题。
 * @date 2021/8/18 8:33
 */
public class ThreadLocalDemo01 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(16);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                String data = new ThreadLocalDemo01().date(finalI);
                System.out.println(data);
            });
        }
        threadPool.shutdown();
    }

    private String date(int seconds){
        Date date = new Date(1000 * seconds);
        SimpleDateFormat dateFormat = ThreadSafeFormater.dateFormatThreadLocal.get();
        return dateFormat.format(date);
    }

}

    class ThreadSafeFormater{
        public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("mm:ss"));
    }
