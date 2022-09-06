package com.imooc.entity.enums;

/**
 * 产品状态
 */
public enum ProductStatus {
    AUDITING("审核中"),

    IN_SELL("销售中"),

    LOCKED("暂停销售"),

    FINISHED("已结束");

    private String desc;

    ProductStatus(String desc){

        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
