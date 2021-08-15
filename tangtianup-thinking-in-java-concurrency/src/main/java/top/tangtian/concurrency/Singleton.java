package top.tangtian.concurrency;

/**
 * @author tangtian
 * @description
 * @date 2021/8/15 9:39
 */
public class Singleton {
    static Singleton instance;
    static Singleton getInstance(){
        if (instance == null){
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
