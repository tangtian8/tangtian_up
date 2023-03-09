package top.tangtian.rocketmq.orderlymessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author tangtian
 * @description
 * @date 2023/3/9 8:12
 */
@AllArgsConstructor
@Data
@ToString
public class ProductOrder {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单类型(订单创建、订单付款、订单完成）
     */
    private String type;
}
