package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageDao extends JpaRepository<Message, Integer> {

    @Query(value = "select * from message where tid in ?1",nativeQuery = true)
    List<Message> findMsg(Iterable<Integer> tids);
}
