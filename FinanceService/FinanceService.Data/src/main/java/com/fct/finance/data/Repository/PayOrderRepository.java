package com.fct.finance.data.Repository;

import com.fct.finance.data.Entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by jon on 2017/4/10.
 */
public interface PayOrderRepository extends JpaRepository<PayOrder, String>{

    PayOrder findBytradeTypeAndtradeId(String tradeType,String tradeId);


}
