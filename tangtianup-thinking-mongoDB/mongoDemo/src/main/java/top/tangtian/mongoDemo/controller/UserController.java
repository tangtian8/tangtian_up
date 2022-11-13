package top.tangtian.mongoDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangtian.mongoDemo.pojo.Users;
import top.tangtian.mongoDemo.repository.UserRepository;

import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/13 17:18
 */
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;



    @GetMapping("/addMongo")
    public String addMongo(){
        Users users = new Users();
        users.setId(String.valueOf(System.currentTimeMillis() / 10000));
        users.setName(users.getId() + "OKK");
        userRepository.save(users);
        return users.getName();
    }


    @GetMapping("/getList")
    public List<Users> getList(){
        List<Users> all = userRepository.findAll();
        return all;
    }
}
