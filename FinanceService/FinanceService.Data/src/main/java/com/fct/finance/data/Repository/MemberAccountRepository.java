package com.fct.finance.data.Repository;


import com.fct.finance.data.Entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jon on 2017/4/7.
 */
public interface MemberAccountRepository extends JpaRepository<MemberAccount, Integer>{

    /**
     * 查询语句findBy后面跟着的名字是entity的属性名称 比如findById Id代表entity UserEntity 里面的id, 也对应表里面的id列
     * sql : select * from memberaccount where memberId = ?
     * @param memberId
     * @return
     */
    MemberAccount findByMemberId(Integer memberId);

    MemberAccount save(MemberAccount account);
    /**
     * 分页查询
     * sql: select * from user where name = ? limit ?, ?
     */
    @Query(nativeQuery = true, value = "select * from user a where a.name =?1 limit ?2,?3")
    List<MemberAccount> findUserByPage(String name, int offset, int limit);

}

