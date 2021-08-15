package top.tangtian;

import java.util.concurrent.TimeUnit;

/**
 * @author tangtian
 * @description
 * @date 2021/8/14 11:13
 */
public class SleepUtils {
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
