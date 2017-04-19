package com.fct.finance.service.business;

//import com.fct.common.logger.LogService;
import com.fct.finance.data.Entity.MemberAccount;
import com.fct.finance.data.Entity.MemberAccountHistory;
import com.fct.finance.data.Entity.PayOrder;
import com.fct.finance.data.Repository.MemberAccountHistoryRepository;
import com.fct.finance.data.Repository.MemberAccountRepository;
import com.fct.finance.data.Repository.PayOrderRepository;
import com.fct.common.exceptions.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jon on 2017/4/10.
 */
public class PayOrderManager  {

    @Autowired
    private PayOrderRepository payOrderRepository;

    @Autowired
    private MemberAccountRepository memberAccountRepository;

    @Autowired
    private MemberAccountHistoryRepository memberAccountHistoryRepository;

    public static PayOrderManager instance = new PayOrderManager();

    @Transactional
    public PayOrder create(PayOrder pay)
    {
        //校验参数
        valid(pay);

        //判读同一业务是否重复生成支付记录
        PayOrder exitOrder =  payOrderRepository.findBytradeTypeAndtradeId(pay.getTradeType(),pay.getTradeId());

        if(exitOrder != null)
        {
            if (exitOrder.getStatus() != Constants.enumPayStatus.waitBuyerPay.getValue())
            {
                throw new IllegalArgumentException("支付请求异常。");
            }
            exitOrder.setPayPlatform(pay.getPayPlatform());
            payOrderRepository.saveAndFlush(exitOrder);
            return pay;
        }
        pay.setStatus(Constants.enumPayStatus.waitBuyerPay.getValue());
        pay.setCreateTime(new Date());
        pay.setOrderId("");

        MemberAccount account = memberAccountRepository.findByMemberId(pay.getMemberId());

        //全部使用积分或账户余额支付
        if (pay.getPayAmount().doubleValue() <= 0 && (pay.getPoints()>0 || pay.getAccountAmount().doubleValue()>0))
        {
            if (account == null)
            {
                throw new BaseException("非法操作");
            }
            if (account.getAvailableAmount().doubleValue() < pay.getAccountAmount().doubleValue())
            {
                throw new BaseException("余额不足");
            }
            // 积分
            if (account.getPoints() < pay.getPoints())
            {
                throw new BaseException("积分不足");
            }
            pay.setPayTime(new Date());
            pay.setStatus(Constants.enumPayStatus.tradeFinished.getValue());
            payOrderRepository.save(pay);

            //可用余额减少
            account.setAvailableAmount(account.getAvailableAmount().subtract(pay.getAccountAmount()));
            //优先扣除充值的余额，如不足才扣除可申请提现金额
            BigDecimal rechargeAmount =  account.getRechargeAmount().subtract(pay.getAccountAmount());
            if(rechargeAmount.doubleValue()>=0)
            {
                account.setRechargeAmount(rechargeAmount);
            }
            else
            {
                //充值余额不可扣，使用账户余额-当前剩余充值余额
                rechargeAmount = pay.getAccountAmount().subtract(account.getRechargeAmount());

                account.setRechargeAmount(new BigDecimal(0)); //充值金额为0

                //可提现金额=当前提现金额-扣除的充值金额
                rechargeAmount =  account.getWithdrawAmount().subtract(rechargeAmount);

                account.setWithdrawAmount(rechargeAmount);
            }

            //减去充值金额或可申请提现金额
            account.setPoints(account.getPoints()-pay.getPoints());

            memberAccountRepository.saveAndFlush(account);

            MemberAccountHistory history = new MemberAccountHistory();
            history.setTradeType(pay.getTradeType());
            history.setTradeId(pay.getTradeId());
            history.setMemberId(account.getMemberId());
            history.setAmount(pay.getAccountAmount());
            history.setBalanceAmount(account.getAvailableAmount());
            history.setPoints(pay.getPoints());
            history.setBalancePoints(account.getPoints());
            history.setRemark(pay.getDesc());
            history.setCreateTime(new Date());

            memberAccountHistoryRepository.save(history);

        }
        else {
            //校验用户是否存在，如不存在则创建memberAccount
            if(account == null)
            {
                account = new MemberAccount();
                account.setMemberId(pay.getMemberId());
                account.setCellPhone(pay.getCellPhone());
                account.setCreateTime(new Date());

                memberAccountRepository.save(account);
            }
            payOrderRepository.save(pay);
        }
        return pay;
    }

    private void valid(PayOrder pay)
    {
        if (StringUtils.isEmpty(pay.getCallbackUrl()))
        {
            throw new NullPointerException("callbackUrl is null");
        }
        if(StringUtils.isEmpty(pay.getCellPhone()))
        {
            throw new NullPointerException("cellphone is null");
        }

    }

    public PayOrder findOne(String orderId)
    {
        return  payOrderRepository.findOne(orderId);
    }

    public PayOrder findByTrade(String tradeType,String tradeId)
    {
        return  payOrderRepository.findBytradeTypeAndtradeId(tradeType,tradeId);
    }

    /// <summary>
    /// 接收到用户通过第三方平台支付成功，处理我们系统相关的业务,并发送消息供业务方处理。
    /// </summary>
    /// <param name="orderId">支付记录Id</param>
    /// <param name="platform">支付平台</param>
    /// <param name="notifyData"></param>
    /// <returns></returns>
    public PayOrder paySuccess(String orderId, String platform, String notifyData)
    {
        PayOrder pay = payOrderRepository.findOne(orderId);

        if(pay ==  null)
        {
            throw new BaseException("请求的支付订单不存在");
        }
        if(pay.getPayTime() !=null )
        {
            //LogService.Info(pay.OrderId + "重复请求该笔已处理过的支付订单。");
            return pay;
        }
        if(StringUtils.isEmpty(platform))
        {
            pay.setPayPlatform(platform);  //更新支付平台
        }
        pay.setPayTime(new Date());
        pay.setNotifyData(notifyData); //写入第三方支付平台异步通知的数据

        MemberAccount account = memberAccountRepository.findByMemberId(pay.getMemberId());
        String logContent = "{支付单号:" + pay.getOrderId() + ",交易Id:" + pay.getTradeId() + "，交易类型:" +
                pay.getTradeType() + "}";

        if ((pay.getAccountAmount().doubleValue() > 0 && account.getAvailableAmount().doubleValue()
                < pay.getAccountAmount().doubleValue())||
                (pay.getPoints()>0 && account.getPoints()<pay.getPoints())) // 余额或积分不足
        {
            // 关闭支付订单
            pay.setStatus(Constants.enumPayStatus.amountException.getValue());

            payOrderRepository.saveAndFlush(pay); //更新支付记录

            /*
            //如使用线上第三方平台支付金额
            if (pay.getPayAmount().doubleValue() > 0) {
                // 充值，将支付金额追加到虚拟账户余额
                account.setAvailableAmount(account.getAvailableAmount().add(pay.getPayAmount()));
            }*/

            // 写余额异常日志
//            LogService.warning(logContent + "第三方支付平台付款成功，但余额不足无法继续交易。");

            //余额异常，通知业务方关闭订单，并生成退款流程（原路返回）。
            SendMessageQ(pay, Constants.enumPayStatus.amountException);
        }
        else // 正常处理
        {
            // 更新支付为支付成功状态
            pay.setStatus(Constants.enumPayStatus.tradeFinished.getValue());
            payOrderRepository.saveAndFlush(pay);

            // 使用虚拟余额或积分支付
            if (pay.getAccountAmount().doubleValue() > 0 || pay.getPoints().doubleValue() > 0)
            {
                account.setAvailableAmount(account.getAvailableAmount().subtract(pay.getAccountAmount()));
                account.setPoints(account.getPoints()-pay.getPoints());
                memberAccountRepository.saveAndFlush(account);

                //消耗账户
                MemberAccountHistory history = new MemberAccountHistory();
                history.setTradeType(pay.getTradeType());
                history.setTradeId(pay.getTradeId());
                history.setMemberId(account.getMemberId());
                history.setAmount(pay.getAccountAmount());
                history.setBalanceAmount(account.getAvailableAmount());
                history.setPoints(pay.getPoints());
                history.setBalancePoints(account.getPoints());
                history.setRemark(pay.getDesc());
                history.setCreateTime(new Date());

                memberAccountHistoryRepository.save(history);
            }

//            LogService.info(logContent + "支付成功。");

            SendMessageQ(pay, Constants.enumPayStatus.tradeFinished);
        }

        return pay;
    }

    private void SendMessageQ(PayOrder payOrder,Constants.enumPayStatus ePayStatue)
    {
//        PaySuccess
    }
}
