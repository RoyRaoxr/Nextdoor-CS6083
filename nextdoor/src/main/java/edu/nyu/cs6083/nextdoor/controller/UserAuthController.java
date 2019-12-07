package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.sql.Timestamp;
import java.util.Date;

@Controller
public class UserAuthController {

    private final Logger logger = LoggerFactory.getLogger(UserAuthController.class.getSimpleName());

    @Autowired
    UserDao userDao;


    @GetMapping("/signup")
    public String signupPage(Model m) {
        m.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/login")
    public String userLogin(@ModelAttribute User user, Model m,
                            HttpServletRequest request) {
        User currUser = userDao.findByUsernameAndPassword(user.getUsername(), DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        if (currUser == null) {
            m.addAttribute("err", true);
            return "index";
        }
        request.getSession().setMaxInactiveInterval(0);
        request.getSession().setAttribute("useradmin", currUser);
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String userLogout(Model m, HttpServletRequest request) {
        User currUser = (User)request.getSession().getAttribute("useradmin");
        if (currUser == null) {
            m.addAttribute("err", true);
            return "index";
        }
        currUser.setLastlogouttime(new Timestamp(new Date().getTime()));
        userDao.save(currUser);
        request.getSession().removeAttribute("useradmin");
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String userSignup(@ModelAttribute User user, Model m) {
        boolean exists = userDao.findByUsername(user.getUsername()) != null;
        if (exists) {
            m.addAttribute("userExists", true);
            return "signup";
        }
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        userDao.save(user);
        return "redirect:/";
    }


}
