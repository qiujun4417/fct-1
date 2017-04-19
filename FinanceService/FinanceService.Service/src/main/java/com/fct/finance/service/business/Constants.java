package com.fct.finance.service.business;

/**
 * Created by jon on 2017/4/10.
 */
public class Constants {

    /// <summary>
    /// 支付状态
    /// </summary>
    public  enum enumPayStatus
    {

        waitBuyerPay(0, "等待买家付款"),tradeFinished(1, "交易完成"),
        tradeClose(2,"交易关闭"),tradeFaild(3,"交易失败"),amountException(4,"交易异常"),
        tradePartRefund(5,"部分退款，一个订单多个商品情况下"),tradeRefund(6,"已退款状态");

        private Integer value;
        private String desc;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private enumPayStatus(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}
