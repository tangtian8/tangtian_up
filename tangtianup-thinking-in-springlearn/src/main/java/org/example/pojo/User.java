package org.example.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author tangtian
 * @description
 * @date 2022/12/6 7:55
 */
@Component
public class User {
    @Autowired
    private Animal animal;

}
