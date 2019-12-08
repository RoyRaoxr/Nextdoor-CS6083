package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockDao extends JpaRepository<Block, Integer> {

}
