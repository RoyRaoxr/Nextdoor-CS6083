package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialController {

    @Autowired
    UserDao userDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/friends")
    public String frinds(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");
        List<Integer> friendids = new ArrayList<>();

        String sql = "Select friendid\n"
            + "        From friends\n"
            + "        Where userid = ? and status = 1\n"
            + "        Union\n"
            + "        select userid as friendid\n"
            + "        From friends\n"
            + "        Where friendid = ? and status = 1";

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, user.getUid());
            return ps;
        }, rs -> {
            friendids.add(rs.getInt("friendid"));
        });

        List<User> allFriends = userDao.findAllById(friendids);

        m.addAttribute("friends", allFriends);
        return "main/friends";
    }

    @GetMapping("/neighbors")
    public String neighbors(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        String sql = "Select neighborid From neighbors Where userid = ?";
        List<Integer> neighborids = new ArrayList<>();

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            return ps;
        }, rs -> {
            neighborids.add(rs.getInt("neighborid"));
        });

        List<User> allNeighbors = userDao.findAllById(neighborids);
        m.addAttribute("neighbors", allNeighbors);
        return "main/neighbors";
    }
}
