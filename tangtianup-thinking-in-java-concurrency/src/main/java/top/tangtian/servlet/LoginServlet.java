package top.tangtian.servlet;

/**
 * @author tangtian
 * @description 模一个Servlet组件
 * @date 2021/8/5 8:21
 */
public class LoginServlet{
    private static String usernameRef;
    private static String passwordRef;

    public static void doPost(String userName, String passWord){
        usernameRef = userName;
        if (userName.equals("a")){
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        passwordRef = passWord;
        System.out.println("userName=" + usernameRef + "--password=" + passwordRef);
    }

}
