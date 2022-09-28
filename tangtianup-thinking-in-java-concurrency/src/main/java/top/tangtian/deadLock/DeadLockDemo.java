package top.tangtian.deadLock;

/**
 * @author tangtian
 * @description
 * @date 2022/9/21 8:13
 */
public class DeadLockDemo {
    public static void main(String[] args) {

        //两个不同的锁对象
        Object obj1 = new Object();
        Object obj2 = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (obj1) {//在拿到锁1的基础上去拿锁2
                    System.out.println(name + "拿到锁1，想要锁2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (obj2) {
                        System.out.println(name + "拿到了锁2");
                    }
                }

            }


        }, "线程A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                synchronized (obj2) {//在拿到锁2的基础上去拿锁1
                    System.out.println(name + "拿到了锁2，想要锁1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (obj1) {
                        System.out.println(name + "拿到了锁1");
                    }
                }


            }
        }, "线程B").start();
    }
}
