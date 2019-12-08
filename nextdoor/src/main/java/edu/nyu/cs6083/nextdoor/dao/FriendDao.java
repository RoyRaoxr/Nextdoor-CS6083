package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Friends;
import edu.nyu.cs6083.nextdoor.bean.FriendsPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendDao extends JpaRepository<Friends, FriendsPK> {

}
