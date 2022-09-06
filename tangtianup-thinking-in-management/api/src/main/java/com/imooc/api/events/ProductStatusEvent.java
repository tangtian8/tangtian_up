package com.imooc.api.events;

import com.imooc.entity.enums.ProductStatus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 产品状态事件
 */
public class ProductStatusEvent implements Serializable{

    private String id;
    private ProductStatus status;

    public ProductStatusEvent(String id, ProductStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
