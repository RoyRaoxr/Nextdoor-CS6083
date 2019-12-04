package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @Autowired
    UserDao userDao;

    @GetMapping("")
    public String ping(Model m) {
        m.addAttribute("user", new User());
        return "index";
    }


    @GetMapping("main")
    public String mainPage() {
        return "main/main";
    }
}
