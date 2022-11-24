package mapper;

import pojo.User;

import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 9:14
 */
public interface UserMapper {
    List<User> findAll();
}
