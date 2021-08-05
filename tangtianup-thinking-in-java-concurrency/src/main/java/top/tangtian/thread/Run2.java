package top.tangtian.thread;


/**
 * @author tangtian
 * @description
 * @date 2021/8/5 6:51
 */
public class Run2 {
    public static void main(String[] args) {
        for (int i = 0;i < 5;i++){
            new Thread(() -> {
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
