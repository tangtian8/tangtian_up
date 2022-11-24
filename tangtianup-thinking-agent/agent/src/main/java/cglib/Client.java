package cglib;

/**
 * @author tangtian
 * @description
 * @date 2022/11/18 9:28
 */
public class Client {
    public static void main(String[] args) {

        System.out.println("**********************CGLibProxy**********************");
        CGLibProxy cgLibProxy = new CGLibProxy();
        IUserManager userManager = (IUserManager) cgLibProxy.createProxyObject(new UserManagerImpl());
        userManager.addUser("lanhuigu", "123456");
    }
}
