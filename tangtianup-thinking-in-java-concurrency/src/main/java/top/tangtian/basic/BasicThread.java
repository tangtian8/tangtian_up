package top.tangtian.basic;

/**
 * @author tangtian
 * @description
 * @date 2021/7/26 7:41
 */
public class BasicThread extends Thread{
    @Override
    public void run() {
     int i = 100;
     for (int j = 100; j>0; j--){
         System.out.println(j);
     }
    }
}
