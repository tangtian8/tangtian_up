package top.tangtian.basic;

/**
 * @author tangtian
 * @description 缓存导致的可见性问题
 * @date 2021/7/26 7:41
 */
public class BasicThreadRunnable implements Runnable{
    private long count = 0;
//    synchronized
    @Override
    public void run() {
        int idx = 0;
        while(idx++ < 1000) {
            count += 1;
        }
        System.out.println(count);
    }

    public BasicThreadRunnable() {
    }
}
