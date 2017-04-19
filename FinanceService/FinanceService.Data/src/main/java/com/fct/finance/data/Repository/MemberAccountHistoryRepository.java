package com.fct.finance.data.Repository;

import com.fct.finance.data.Entity.MemberAccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jon on 2017/4/10.
 */
public interface MemberAccountHistoryRepository extends JpaRepository<MemberAccountHistory, Long> {

}
