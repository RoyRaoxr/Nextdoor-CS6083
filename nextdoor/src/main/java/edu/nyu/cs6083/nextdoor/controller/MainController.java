package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class.getSimpleName());

    @Autowired
    UserDao userDao;

    @GetMapping("")
    public String ping(Model m) {
        m.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/signup")
    public String signupPage(Model m) {
        m.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/login")
    public String userLogin(@ModelAttribute User user, Model m) {
        User currUser = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (currUser == null) {
            m.addAttribute("err", true);
            return "index";
        }

        return "login200";
    }

    @PostMapping("/signup")
    public String userSignup(@ModelAttribute User user, Model m) {
        boolean exists = userDao.findByUsername(user.getUsername()) != null;
        if (exists) {
            m.addAttribute("userExists", true);
            return "signup";
        }

        userDao.save(user);

        return "redirect:/";
    }
}
