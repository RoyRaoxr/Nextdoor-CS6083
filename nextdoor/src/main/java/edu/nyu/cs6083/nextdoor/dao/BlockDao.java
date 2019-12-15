package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockDao extends JpaRepository<Block, Integer> {

    @Query(
            value = "select * from block where bid in (select bid from application where uid=?1 and status=0)",
            nativeQuery = true)
    List<Block> findAllApplied(Integer uid);

    @Query(
            value = "select * from block where bid not in (select bid from application where uid=?1 and status=0)",
            nativeQuery = true)
    List<Block> findAllCanJoin(Integer uid);
}
