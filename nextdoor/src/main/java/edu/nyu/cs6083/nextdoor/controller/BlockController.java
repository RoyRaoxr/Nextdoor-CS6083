package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.Application;
import edu.nyu.cs6083.nextdoor.bean.Block;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.ApplicationDao;
import edu.nyu.cs6083.nextdoor.dao.ApplicationVoteDao;
import edu.nyu.cs6083.nextdoor.dao.BlockDao;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlockController {


    @Autowired
    BlockDao blockDao;

    @Autowired
    UserDao userDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ApplicationDao applicationDao;

    @Autowired
    ApplicationVoteDao applicationVoteDao;

    @GetMapping("block")
    public String block(HttpServletRequest request, Model m) {

        User user = (User) request.getSession().getAttribute("useradmin");

        var sql = "select bid from joinblock where uid = ?";

        AtomicInteger bid = new AtomicInteger(-1);
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            return ps;
        }, rs -> {
            bid.set(rs.getInt("bid"));
        });

        if (bid.get() == -1) {
            m.addAttribute("ex", false);
        } else {
            Block block = blockDao.findById(bid.get()).get();

            List<Application> appU = applicationDao.listAllApply(bid.get(), user.getUid());

            m.addAttribute("appU", appU);
            m.addAttribute("block", block);
            m.addAttribute("ex", true);
        }
        List<Block> otherB = blockDao.findAllCanJoin(user.getUid());
        List<Block> appliedB = blockDao.findAllApplied(user.getUid());
        m.addAttribute("other", otherB);
        m.addAttribute("applied", appliedB);

        return "main/block";
    }

    @GetMapping("voteapp")
    public String vote(
        HttpServletRequest request,
        @RequestParam("id") Integer uid,
        @RequestParam("s") Integer status,
        @RequestParam("bid") Integer bid,
        @RequestParam("aid") Integer aid) {

        User user = (User) request.getSession().getAttribute("useradmin");

        String sql = "INSERT INTO `nextdoor`.`applicationvote`(`aid`, `voteid`, `timestamp`, `status`) VALUES (?, ?, now(), ?)";

        jdbcTemplate.update(sql, aid, user.getUid(), status);

        String p2 = "call can_join(?,?)";

        List<SqlParameter> ps = Arrays.asList(
            new SqlParameter(Types.INTEGER), new SqlParameter(Types.INTEGER),
            new SqlParameter(Types.INTEGER), new SqlParameter(Types.INTEGER));

        jdbcTemplate.call(con -> {
            CallableStatement cs = con.prepareCall(p2);
            cs.setInt(1, uid);
            cs.setInt(2, bid);
            return cs;
        }, ps);

        return "redirect:block";
    }

    @GetMapping("joinblock")
    public String join(@RequestParam("id") Integer bid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("useradmin");

        Integer prevAid = jdbcTemplate
            .queryForObject("select COALESCE(max(aid),0) from application", Integer.class);
        String sql =
            "INSERT INTO `nextdoor`.`application`(`aid`, `uid`, `bid`, `timestamp`, `status`) "
                + "VALUES (?, ?, ?, now(), 0)";
        jdbcTemplate.update(sql, prevAid + 1, user.getUid(), bid);

        return "redirect:block";
    }


    @GetMapping("leaveblock")
    public String leave(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("useradmin");

        String p = "call leave_block(?)";
        List<SqlParameter> ps = new ArrayList<>();

        jdbcTemplate.call(con -> {
            CallableStatement cs = con.prepareCall(p);
            cs.setInt(1, user.getUid());
            return cs;
        }, ps);

        return "redirect:block";
    }
}