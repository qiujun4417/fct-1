package com.fct.finance.data.Repository;

import com.fct.finance.data.Entity.PayOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jon on 2017/4/10.
 */
public interface PayOrderRepository extends JpaRepository<PayOrder, String>{

    PayOrder findBytradeTypeAndtradeId(String tradeType,String tradeId);

    Page<PayOrder> findAll(Specification<PayOrder> spec, Pageable pageable);  //分页按条件查询
}
