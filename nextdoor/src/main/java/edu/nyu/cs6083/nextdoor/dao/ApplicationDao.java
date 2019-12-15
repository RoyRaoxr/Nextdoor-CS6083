package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationDao extends JpaRepository<Application, Integer> {

    @Query(
        value = "select * from application where bid = "
            + "?1 and status = 0 and aid not in (select aid from applicationvote where voteid=?2)",
        nativeQuery = true)
    List<Application> listAllApply(Integer bid, Integer uid);
}
