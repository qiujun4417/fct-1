package com.fct.finance.service;

import com.fct.finance.data.Entity.*;
import com.fct.finance.service.business.PayOrderManager;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by jon on 2017/4/7.
 */
@Service(value = "financeService")
public class FinanceService implements com.fct.finance.interfaces.FinanceService {

    /*@Autowired
    private MemberService memberService;*/
    @Override
    public PayOrder createPayOrder(PayOrder payOrder) {

        return PayOrderManager.instance.create(payOrder);
    }

    @Override
    public PayOrder getPayOrder(String orderId){
        return  PayOrderManager.instance.findOne(orderId);
    }

    @Override
    public PayOrder getPayOrderByTrade(String tradeType,String tradeId)
    {
        return  PayOrderManager.instance.findByTrade(tradeType,tradeId);
    }

    @Override
    public PayOrder paySuccess(String orderId,String platform,String notifyData){
        return  null;
    }

    @Override
    public List<PayOrder> findPayRecord(Integer memberId, String cellPhone, String platform, String tradeId, String tradeType,
                                 Integer pageIndex, Integer pageSize)
    {
        return  null;
    }

    @Override
    public MemberAccount getMemberAccount(Integer memberId)
    {
        return  null;
    }

    @Override
    public List<MemberAccount> findMemberAccount(String cellPhone,Integer orderBy,Integer pageIndex,Integer pageSize)
    {
        return  null;
    }

    @Override
    public List<MemberAccountHistory> findMemberAccountHistory(Integer memberId, String tradeId, String tradeType,
                                                        Integer pageIndex, Integer pageSize)
    {
        return  null;
    }
    @Override
    public RefundRecord createRefundRecord(RefundRecord refund)
    {
        return  null;
    }

    @Override
    public void financialConfirmRefund(String ids)
    {

    }

    @Override
    public void refundSuccess(Long refundId,String notifyData)
    {

    }

    @Override
    public List<RefundRecord> findRefundRecord(Integer memberId,String cellPhone,String tradeId,String tradeType,String payPlatform,
                                        Integer status,String beginTime,String endTime,Integer pageIndex,Integer pageSize)
    {
        return  null;
    }
    @Override
    public void applyWithdraw(WithdrawRecord withdrawRecord)
    {

    }

    @Override
    public void withdrawSuccess(Integer id)
    {

    }

    @Override
    public void withdrawFail(Integer id,String desc)
    {

    }

    @Override
    public List<WithdrawRecord> findWithdrawRecord(Integer memberId, String cellPhone, Integer status,
                                            Integer pageIndex, Integer pageSize)
    {
        return  null;
    }
    @Override
    public Integer createSettleRecord(SettleRecord settleRecord)
    {
        return  null;
    }

    @Override
    public void financeConfirmSettle(String ids)
    {

    }

    @Override
    public void refuseSettle(Integer id,String remark)
    {

    }

    @Override
    public SettleRecord getSettleRecord(Integer recordId)
    {
        return  null;
    }

    @Override
    public List<SettleRecord> findSettleRecord(Integer memberId, String tradeType, String tradeId, Integer status,
                                        String beginTime, String entTime, Integer pageIndex,Integer pageSize)
    {
        return  null;
    }
    @Override
    public List<PayPlatform> findPayPlatform()
    {
        return  null;
    }

}
