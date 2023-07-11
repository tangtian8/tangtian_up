/**
 * @author tangtian
 * @description
 * @date 2023/7/10 18:51
 */
public class TestBasicParam {

        public static void main(String[] args) {
            int num = 3;
            System.out.println("修改前的num值:"+num);
            changeValue(num);
            System.out.println("修改后的num值:"+num);
        }
        private static void changeValue(int num) {
            num = 5;
            System.out.println("形参num值:"+num);
        }

}
