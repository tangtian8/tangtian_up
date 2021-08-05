package top.tangtian.servlet;

/**
 * @author tangtian
 * @description
 * @date 2021/8/5 8:29
 */
public class Runable2 implements Runnable{
    @Override
    public void run() {
        LoginServlet.doPost("b","bb");
    }
}
