package edu.nyu.cs6083.nextdoor.controller;

import edu.nyu.cs6083.nextdoor.bean.Message;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.MessageDao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @GetMapping("message")
    public String message(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");
        return "main/message";
    }
}
