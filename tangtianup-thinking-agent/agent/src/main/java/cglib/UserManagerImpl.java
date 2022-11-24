package cglib;

/**
 * @author tangtian
 * @description
 * @date 2022/11/18 9:29
 */
public class UserManagerImpl implements IUserManager{
    @Override
    public void addUser(String id, String password) {
        System.out.println("======调用了UserManagerImpl.addUser()方法======");
    }
}
