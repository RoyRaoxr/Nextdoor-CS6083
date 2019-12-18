package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserProfileController {

    private static String API = "https://maps.googleapis.com/maps/api/geocode/json?"
        + "address=%s,+%s,+%s&key=AIzaSyCRaTJcMX8c_Y0T-lOwpX9bWhQeWh8yAXE&callback";
    @Autowired
    UserDao userDao;

    @GetMapping("/profile")
    public String userProfile(Model m, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("useradmin");
        m.addAttribute("user", user);
        return "main/profile";
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute User user, HttpServletRequest request)
        throws Exception {
        User oldUser = userDao.findByUsername(user.getUsername());
        if (user.getPassword().isBlank()) {
            user.setPassword(oldUser.getPassword());
        }
        user.setUid(oldUser.getUid());

        HttpClient client = HttpClient.newHttpClient();
        String fetch = String.format(API, user.getUstreet().replaceAll(" ", "+"),
            user.getUcity().replaceAll(" ", "+"), user.getUstate());
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(fetch)).build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray arr = jsonObject.getJSONArray("results");

        Double lat = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
            .getDouble("lat");
        Double lng = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
            .getDouble("lng");

        user.setLat(lat);
        user.setLng(lng);
        userDao.save(user);

        request.getSession().setAttribute("useradmin", user);
        return "redirect:/main";
    }
}
