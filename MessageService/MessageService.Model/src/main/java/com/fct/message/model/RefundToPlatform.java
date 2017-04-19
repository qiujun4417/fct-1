package com.fct.message.model;

import java.math.BigDecimal;

/**
 * Created by jon on 2017/4/11.
 */
public class RefundToPlatform {

    /// <summary>
    /// 支付订单号
    /// </summary>
    public String pay_orderid;

    /// <summary>
    /// 退款订单号
    /// </summary>
    public Long refund_id;

    /// <summary>
    /// 支付金额
    /// </summary>
    public BigDecimal pay_amount;

    /// <summary>
    /// 退款金额
    /// </summary>
    public BigDecimal refund_amount;

    /// <summary>
    /// 支付平台方式
    /// </summary>
    public String pay_platform;

    /// <summary>
    /// 备注
    /// </summary>
    public String desc;
}
