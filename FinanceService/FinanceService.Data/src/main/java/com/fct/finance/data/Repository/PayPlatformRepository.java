package com.fct.finance.data.Repository;

import com.fct.finance.data.Entity.PayPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jon on 2017/4/21.
 */
public interface PayPlatformRepository  extends JpaRepository<PayPlatform, Integer> {


}
