package edu.nyu.cs6083.nextdoor.dao;

import edu.nyu.cs6083.nextdoor.bean.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeighborhoodDao extends JpaRepository<Neighborhood, Integer> {

}
