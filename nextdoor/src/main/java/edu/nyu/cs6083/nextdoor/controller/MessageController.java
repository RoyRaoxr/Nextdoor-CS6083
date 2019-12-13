package edu.nyu.cs6083.nextdoor.controller;

import edu.nyu.cs6083.nextdoor.bean.Message;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.MessageDao;
import edu.nyu.cs6083.nextdoor.dao.ThreadDao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageDao messageDao;

    @Autowired
    ThreadDao threadDao;

    @GetMapping("reply")
    public String reply(HttpServletRequest request, Model m, @RequestParam("id") Integer mid) {
        User user = (User) request.getSession().getAttribute("useradmin");
        Message replyMsg = messageDao.findById(mid).get();

        String sql = "select mid from message where mid != ? and tid = ?";

        List<Integer> mids = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mid);
            ps.setInt(2, replyMsg.getMsgThread().getTid());
            return ps;
        }, rs -> {
            mids.add(rs.getInt("mid"));
        });
        List<Message> sameThreadMsg = messageDao.findAllById(mids);

        m.addAttribute("msg", replyMsg);
        m.addAttribute("all", sameThreadMsg);
        return "main/reply";
    }

    @PostMapping("replysmsg")
    public String replyMsg() {
        return "login200";
    }

    @GetMapping("message")
    public String message(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        //initial message mid
        String sql = "Select mid as mid from message Where tid in \n"
            + "(Select tid From threadparticipant natural join thread Where recid = ? and type = ?)";

        //type 3 block
        List<Integer> type3 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 3);
            return ps;
        }, rs -> {
            type3.add(rs.getInt("mid"));
        });
        List<Message> type3s = messageDao.findAllById(type3);

        // type 2 hood
        List<Integer> type2 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 2);
            return ps;
        }, rs -> {
            type2.add(rs.getInt("mid"));
        });
        List<Message> type2s = messageDao.findAllById(type2);

//
        //type 1 friends
        List<Integer> type1 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 1);
            return ps;
        }, rs -> {
            type1.add(rs.getInt("mid"));
        });
        List<Message> type1s = messageDao.findAllById(type1);

        //type 0 neighbors
        List<Integer> type0 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 0);
            return ps;
        }, rs -> {
            type0.add(rs.getInt("mid"));
        });
        List<Message> type0s = messageDao.findAllById(type0);

        m.addAttribute("t3", type3s);
        m.addAttribute("t2", type2s);
        m.addAttribute("t1", type1s);
        m.addAttribute("t0", type0s);
        return "main/message";
    }
}
