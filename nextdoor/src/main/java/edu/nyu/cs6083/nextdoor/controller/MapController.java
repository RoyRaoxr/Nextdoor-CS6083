package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.Block;
import edu.nyu.cs6083.nextdoor.bean.Neighborhood;
import edu.nyu.cs6083.nextdoor.dao.BlockDao;
import edu.nyu.cs6083.nextdoor.dao.NeighborhoodDao;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MapController {

    @Autowired
    BlockDao blockDao;

    @Autowired
    NeighborhoodDao neighborhoodDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("map")
    public String map() {
        return "main/map";
    }


    @GetMapping("fetchblock")
    public @ResponseBody
    Block fetchBlock(@RequestParam("uid") Integer uid) {
        return findBlockByUid(uid);
    }

    @GetMapping("fetchhood")
    public @ResponseBody
    Neighborhood fetchHood(@RequestParam("uid") Integer uid) {
        return findHoodByUid(uid);
    }

    private Block findBlockByUid(Integer uid) {
        String sql = "select bid from joinblock where uid = ?";
        AtomicInteger bid = new AtomicInteger();

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            return ps;
        }, rs -> {
            bid.set(rs.getInt("bid"));
        });

        Block block = blockDao.findById(bid.get()).get();
        return block;
    }

    private Neighborhood findHoodByUid(Integer uid) {
        String sql = "SELECT nid from neighborhood WHERE nid = (SELECT nid from joinblock NATURAL join block where uid = ?)";

        AtomicInteger nid = new AtomicInteger();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            return ps;
        }, rs -> {
            nid.set(rs.getInt("nid"));
        });

        Neighborhood neighborhood = neighborhoodDao.findById(nid.get()).get();
        return neighborhood;
    }

}
