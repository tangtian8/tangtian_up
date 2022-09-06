package com.imooc.seller;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/18 0018.
 */
@Component
public class HazelcastMapTest {

    @Autowired
    private HazelcastInstance hazelcastInstance;

//    @PostConstruct
    public void put(){
        Map map = hazelcastInstance.getMap("imooc");
        map.put("name","imooc");
    }
}
