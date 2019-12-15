package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.ApplicationVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationVoteDao extends JpaRepository<ApplicationVote, Integer> {


}
