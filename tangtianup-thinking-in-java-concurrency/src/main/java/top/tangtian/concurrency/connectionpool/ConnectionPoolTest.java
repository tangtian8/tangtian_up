package top.tangtian.concurrency.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangtian
 * @description
 * @date 2021/8/20 7:31
 */
public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    //保证所有ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);
    //main线程将会等待所有ConnectionRunner结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        //线程数量,可以修改线程数量进行观察
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger noGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++){
            Thread thread = new Thread(new ConnectionRunner(count,got,noGot));
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount + count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection: " + noGot);
    }

    static class ConnectionRunner implements Runnable{
        int count;
        AtomicInteger got;
        AtomicInteger noGot;

        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger noGot) {
            this.count = count;
            this.got = got;
            this.noGot = noGot;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            while (count > 0){
                //从线程池中获取连接,如果1000ms内无法获取,将会返回null
                //分别统计连接池获取的数量got和未获取到的数量notGot;
                try {
                    Connection connection = pool.fetchConnection(1000);
                    if (connection != null){
                        try {
                            connection.createStatement();
                            connection.commit();
                        }finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }else {
                        noGot.incrementAndGet();
                    }
                } catch (InterruptedException | SQLException interruptedException) {
                    interruptedException.printStackTrace();
                }finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
