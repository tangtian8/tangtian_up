package top.tangtian.concurrency;


/**
 * @author tangtian
 * @description
 * @date 2021/8/15 8:44
 */
public class Join {
    public static void main(String[] args) {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++){
            //每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous),String.valueOf(i));
            thread.start();
            previous = thread;
        }
    }
    static class Domino implements Runnable{
        private Thread thread;
        public Domino(Thread thread){
            this.thread = thread;
        }
        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminate");
        }
    }
}
