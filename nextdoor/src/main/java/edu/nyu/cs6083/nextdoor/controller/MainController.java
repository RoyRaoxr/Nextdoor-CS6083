package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    UserDao userDao;

    @GetMapping("")
    public String ping(@ModelAttribute User user) {
        return "index";
    }

    @RequestMapping("/login")
    public String userLogin(@RequestParam Map<String, String> body, Model m) {
        User user = userDao.findByUsernameAndPassword(body.get("username"), body.get("password"));
        System.out.println(user.getFirstname());
        if (user != null) {
            m.addAttribute("users", user);
            return "listUsers";
        }

        return "login200";

    }
}
