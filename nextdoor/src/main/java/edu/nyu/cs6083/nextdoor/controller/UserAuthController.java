package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserAuthController {

    private static String API = "https://maps.googleapis.com/maps/api/geocode/json?"
        + "address=%s,+%s,+%s&key=AIzaSyCRaTJcMX8c_Y0T-lOwpX9bWhQeWh8yAXE&callback";
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
        User currUser = userDao.findByUsernameAndPassword(user.getUsername(),
            DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
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
        User currUser = (User) request.getSession().getAttribute("useradmin");
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
    public String userSignup(@ModelAttribute User user, Model m) throws Exception {
        boolean exists = userDao.findByUsername(user.getUsername()) != null;
        if (exists) {
            m.addAttribute("userExists", true);
            return "signup";
        }
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);

        HttpClient client = HttpClient.newHttpClient();
        String fetch = String.format(API, user.getUstreet().replaceAll(" ", "+"),
            user.getUcity().replaceAll(" ", "+"), user.getUstate());
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fetch)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray arr = jsonObject.getJSONArray("results");

        Double lat = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
            .getDouble("lat");
        Double lng = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
            .getDouble("lng");

        user.setLat(lat);
        user.setLng(lng);

        userDao.save(user);
        return "redirect:/";
    }


}
