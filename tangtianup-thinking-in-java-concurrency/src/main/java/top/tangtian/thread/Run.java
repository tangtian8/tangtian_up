package top.tangtian.thread;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 6:39
 */
public class Run {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        //耗时大
        //start方法耗时原因是执行了多个步骤
        //1.通过JVM告诉操作系统创建Thread
        //2.操作系统开辟内存并使用Windows SDK中createThread()函数创建Thread线程对象
        //3.操作系统对Thread对象进行调度,以确定执行时机
        //4.Thread在操作系统中被成功执行
        myThread.start();
        //耗时小
        System.out.println("运行结束");
    }
}
