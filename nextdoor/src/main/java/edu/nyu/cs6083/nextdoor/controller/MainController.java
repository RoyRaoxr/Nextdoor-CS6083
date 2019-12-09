package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
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

@Controller
public class MainController {


    @Autowired
    ThreadDao threadDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("")
    public String ping(HttpServletRequest request, Model m) {
        if (request.getSession().getAttribute("useradmin") == null) {
            m.addAttribute("user", new User());
            return "index";
        }

        return "redirect:/main";

    }


    @GetMapping("main")
    public String mainPage(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        String sql = "Select distinct tid \n"
            + "From message Where message.timestamp > (Select lastlogouttime From user Where uid = ?) \n"
            + "and tid in (Select tid From threadparticipant natural join thread Where recid = ? and type = 3)\n";

        List<Integer> threadIds = new ArrayList<>();

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, user.getUid());
            return ps;
        }, rs -> {
            threadIds.add(rs.getInt("tid"));
        });

        System.out.println(threadIds);

        m.addAttribute("tids", threadIds);
        return "main/main";
    }
}
