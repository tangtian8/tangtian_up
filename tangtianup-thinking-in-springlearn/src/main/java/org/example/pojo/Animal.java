package org.example.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tangtian
 * @description
 * @date 2022/12/6 7:55
 */
@Component
public class Animal {
    @Autowired
    private User user;

//    public Animal(User user) {
//        this.user = user;
//    }
}
