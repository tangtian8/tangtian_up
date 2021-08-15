package top.tangtian.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2021/8/15 9:02
 */
public class Profiler {
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue(){
            return System.currentTimeMillis();
        }
    };


    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end(){
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost:" + Profiler.end() + " mills");
    }
}
