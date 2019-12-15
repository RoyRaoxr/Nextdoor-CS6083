package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageDao extends JpaRepository<Message, Integer> {

    @Query(value = "select * from message where mid in ?1", nativeQuery = true)
    List<Message> findMsg(Iterable<Integer> mids);


    @Query(
        value = "Select * From threadparticipant natural join message Where recid = ?1 and text LIKE concat('%', ?2, '%')",
        nativeQuery = true)
    List<Message> listByKey(Integer uid, String keyword);
}

