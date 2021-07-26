package top.tangtian.basic;

/**
 * @author tangtian
 * @description
 * @date 2021/7/26 7:41
 */
public class BasicThreadRunnable implements Runnable{
    private Integer a = 10;

//    synchronized
    @Override
    public   void  run() {
        for (int i = 0; i < 5; i++) {
            a--;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
            System.out.println(Thread.currentThread().getName() + " → a = " + a);
        }
    }

    public BasicThreadRunnable() {
    }
}
