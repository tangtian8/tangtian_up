package top.tangtian.thread1;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 7:39
 */
public class MyThread extends Thread{
    private int count = 5;

    public MyThread(String name){
        super();
        this.setName(name);
    }

    @Override
    public void run() {
        super.run();
        while (count > 0){
            count--;
            System.out.println(currentThread().getName() + "计算 count = " + count );
        }
    }
}
