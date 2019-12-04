package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class.getSimpleName());

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
        User currUser = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (currUser == null) {
            m.addAttribute("err", true);
            return "index";
        }
        request.getSession().setAttribute("useradmin", currUser);
        return "redirect:/main";
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
