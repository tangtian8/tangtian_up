package top.tangtian.basic;

/**
 * @author tangtian
 * @description
 * @date 2021/8/2 7:03
 */
public class VolatileExample implements Runnable{
    int x = 0;
    volatile boolean v = false;
    @Override
    public void run() {
        x = 42;
        v = true;
    }
}
