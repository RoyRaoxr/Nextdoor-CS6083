package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface UserDao extends JpaRepository<User, Integer> {


    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

    @Query(value = "Insert into friends Values (?1, ?2, 0, (Select nid from joinblock natural join block where uid = ?1))", nativeQuery = true)
    void createApplication(int userid, int fid);

}
