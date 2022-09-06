package com.imooc.entity.enums;

/**
 * 订单类型
 */
public enum OrderType {
    APPLY("申购"),
    REDEEM("赎回");

    private String desc;

    OrderType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
