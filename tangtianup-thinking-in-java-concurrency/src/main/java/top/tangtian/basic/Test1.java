package top.tangtian.basic;

/**
 * @author tangtian
 * @description 创建JVM虚拟进程
 * @date 2021/8/5 6:31
 */
public class Test1 {
    public static void main(String[] args) {
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
