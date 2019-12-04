package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserProfileController {


    @Autowired
    UserDao userDao;

    @GetMapping("/profile")
    public String userProfile(Model m, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("useradmin");
        m.addAttribute("user", user);
        return "main/profile";
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute User user, HttpServletRequest request) {
        User oldUser = userDao.findByUsername(user.getUsername());
        if (user.getPassword().isBlank()) {
            user.setPassword(oldUser.getPassword());
        }
        user.setUid(oldUser.getUid());
        userDao.save(user);

        request.getSession().setAttribute("useradmin", user);
        return "redirect:/main";
    }
}
