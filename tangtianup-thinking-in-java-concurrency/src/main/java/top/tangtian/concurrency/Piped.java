package top.tangtian.concurrency;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @author tangtian
 * @description 管道输入/输出流
 * @date 2021/8/18 7:23
 */
public class Piped {
    public static void main(String[] args) throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        //将输出流和输入流进行连接,否则在使用的时候会抛出IOException
        out.connect(in);
        Thread thread = new Thread(new Print(in),"PrintThread");
        thread.start();
        int receive = 0;
        try {
            while ((receive = System.in.read()) != -1){
                out.write(receive);
            }
        }finally {
            out.close();
        }
    }

    static class Print implements Runnable{
        private PipedReader in;
        public Print(PipedReader in){
            this.in = in;
        }
        @Override
        public void run() {
            int receive = 0;
            while (true) {
                try {
                    if ((receive = in.read()) != -1){
                        System.out.print((char)receive);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
