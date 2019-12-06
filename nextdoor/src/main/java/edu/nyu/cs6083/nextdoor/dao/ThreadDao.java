package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.MsgThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadDao extends JpaRepository<MsgThread, Integer> {

}
