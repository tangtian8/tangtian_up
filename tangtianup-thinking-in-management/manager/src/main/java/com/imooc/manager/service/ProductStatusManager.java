package com.imooc.manager.service;

import com.imooc.api.events.ProductStatusEvent;
import com.imooc.entity.enums.ProductStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 管理产品状态
 */
@Component
public class ProductStatusManager {
    static  final String MQ_DESTINATION = "VirtualTopic.PRODUCT_STATUS";
    static Logger LOG = LoggerFactory.getLogger(ProductStatusManager.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void changeStatus(String id, ProductStatus status){
        ProductStatusEvent event = new ProductStatusEvent(id,status);
        LOG.info("send message:{}",event);
        jmsTemplate.convertAndSend(MQ_DESTINATION,event);
    }
//    @PostConstruct
    public void init(){
        changeStatus("001",ProductStatus.IN_SELL);
    }
}
