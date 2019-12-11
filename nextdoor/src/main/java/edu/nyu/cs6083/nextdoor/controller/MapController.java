package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.Block;
import edu.nyu.cs6083.nextdoor.dao.BlockDao;
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
    JdbcTemplate jdbcTemplate;

    @GetMapping("map")
    public String map() {
        return "main/map";
    }


    @GetMapping("/ping")
    public @ResponseBody
    Block pong(@RequestParam("uid") Integer uid) {

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
}
