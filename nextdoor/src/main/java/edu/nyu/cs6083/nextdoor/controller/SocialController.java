package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.FriendDao;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SocialController {

    @Autowired
    UserDao userDao;

    @Autowired
    FriendDao friendDao;

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

        List<Integer> sameHoodids = new ArrayList<>();
        String listSameHood = "Select uid \n"
            + "From joinblock natural join block \n"
            + "Where \n"
            + "nid = (Select nid From joinblock natural join block where uid = ?) and \n"
            + "uid != ? and (uid not in (Select friendid From friends Where userid = ? Union Select userid as friendid From friends Where friendid = ?))";

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(listSameHood);
            ps.setInt(1, user.getUid());
            ps.setInt(2, user.getUid());
            ps.setInt(3, user.getUid());
            ps.setInt(4, user.getUid());
            return ps;
        }, rs -> {
            sameHoodids.add(rs.getInt("uid"));
        });

        List<User> sameHoodToAdd = userDao.findAllById(sameHoodids);
        m.addAttribute("hoods", sameHoodToAdd);

        String firendReq = "Select friendid From friends Where userid = ? \n"
            + "and status = 0 Union Select userid as friendid From friends Where friendid = ? and status = 0";

        List<User> toApprove = findPending(user);
        List<User> friendReq = friendReqFrom(user);
        m.addAttribute("toApprove", toApprove);
        m.addAttribute("pendings", friendReq);
        return "main/friends";
    }

    private List<User> findPending(User user) {
        String firendReq = "Select userid From friends Where friendid = ? and status = 0";
        List<Integer> fromIds = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(firendReq);
            ps.setInt(1, user.getUid());
            return ps;
        }, rs -> {
            fromIds.add(rs.getInt("userid"));
        });

        List<User> ret = userDao.findAllById(fromIds);
        return ret;
    }

    private List<User> friendReqFrom(User user) {
        String firendReq = "Select friendid From friends Where userid = ? and status = 0";
        List<Integer> toIds = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(firendReq);
            ps.setInt(1, user.getUid());
            return ps;
        }, rs -> {
            toIds.add(rs.getInt("friendid"));
        });

        List<User> ret = userDao.findAllById(toIds);
        return ret;
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

        String listSameHood = "Select uid \n"
            + "From joinblock natural join block \n"
            + "Where \n"
            + "nid = (Select nid From joinblock natural join block where uid = ?) and \n"
            + "uid != ? and (uid not in (Select neighborid From neighbors Where userid = ? Union Select userid as neighborid From neighbors Where neighborid = ?))";

        List<Integer> sameHood = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(listSameHood);
            ps.setInt(1, user.getUid());
            ps.setInt(2, user.getUid());
            ps.setInt(3, user.getUid());
            ps.setInt(4, user.getUid());
            return ps;
        }, rs -> {
            sameHood.add(rs.getInt("uid"));
        });
        List<User> inHood = userDao.findAllById(sameHood);
        m.addAttribute("samehood", inHood);
        return "main/neighbors";
    }

    @GetMapping("/addfriend")
    public String addFriend(HttpServletRequest request, @RequestParam("id") Integer uid, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        String sql = "Insert into friends Values (?, ?, 0, (Select nid from joinblock natural join block where uid = ?))";
        //userDao.createFriendsApplication(user.getUid(), uid);
        jdbcTemplate.update(sql, user.getUid(), uid, user.getUid());
        return "redirect:/friends";

    }

    @GetMapping("/addneighbors")
    public String addNeighbor(HttpServletRequest request, @RequestParam("id") Integer uid,
        Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        String sql = "Insert into neighbors values (?, ?)";
        //userDao.createFriendsApplication(user.getUid(), uid);
        jdbcTemplate.update(sql, user.getUid(), uid);
        return "redirect:/neighbors";

    }

    @GetMapping("/acceptfriend")
    public String acceptFriend(HttpServletRequest request, @RequestParam("id") Integer uid,
        @RequestParam("s") Integer s, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");
        if (s == 1) {
            String sql = "Update friends set status = 1 where userid = ? and friendid = ?";
            jdbcTemplate.update(sql, uid, user.getUid());
        } else {
            String sql = "Delete from friends where userid = ? and friendid = ?";
            jdbcTemplate.update(sql, uid, user.getUid());
        }
        return "redirect:/friends";
    }
}
