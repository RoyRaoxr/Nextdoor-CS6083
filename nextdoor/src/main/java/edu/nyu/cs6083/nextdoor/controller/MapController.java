package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.Block;
import edu.nyu.cs6083.nextdoor.bean.Latlng;
import edu.nyu.cs6083.nextdoor.bean.Message;
import edu.nyu.cs6083.nextdoor.bean.Neighborhood;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.BlockDao;
import edu.nyu.cs6083.nextdoor.dao.MessageDao;
import edu.nyu.cs6083.nextdoor.dao.NeighborhoodDao;
import edu.nyu.cs6083.nextdoor.dao.UserDao;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MapController {

    private HttpClient client = HttpClient.newHttpClient();
    private static String API = "https://maps.googleapis.com/maps/api/geocode/json?"
        + "address=%s,+%s,+%s&key=AIzaSyCRaTJcMX8c_Y0T-lOwpX9bWhQeWh8yAXE&callback";

    @Autowired
    BlockDao blockDao;

    @Autowired
    NeighborhoodDao neighborhoodDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @GetMapping("map")
    public String map(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");
        return "main/map";
    }


    @GetMapping("fetchmessage")
    public @ResponseBody
    List<Latlng> fetchMsg(@RequestParam("uid") Integer uid) {
        List<Message> msg = findAllMsg(uid);
        List<Latlng> ret = new ArrayList<>();
        for (Message m : msg) {
            ret.add(new Latlng(m.getLat(), m.getLng()));
        }

        return ret;
    }

    @GetMapping("fetchuser")
    public @ResponseBody
    List<Latlng> fetchUser(@RequestParam("uid") Integer uid) throws Exception {
        List<Integer> sameHoodids = new ArrayList<>();

        String listSameHood = "SELECT uid from joinblock natural join block "
            + "where nid = (Select nid From joinblock natural join block where uid = ?) and uid != ?";

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(listSameHood);
            ps.setInt(1, uid);
            ps.setInt(2, uid);
            return ps;
        }, rs -> {
            sameHoodids.add(rs.getInt("uid"));
        });

        List<User> sameHoodToAdd = userDao.findAllById(sameHoodids);
        List<Latlng> ret = new ArrayList<>();

        for (User user : sameHoodToAdd) {
            String fetch = String.format(API, user.getUstreet().replaceAll(" ", "+"),
                user.getUcity().replaceAll(" ", "+"), user.getUstate());
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fetch)).build();
            HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray arr = jsonObject.getJSONArray("results");

            Double lat = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");
            Double lng = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

            ret.add(new Latlng(lat, lng));

        }

        return ret;

    }

    @GetMapping("fetchblock")
    public @ResponseBody
    Block fetchBlock(@RequestParam("uid") Integer uid) {
        return findBlockByUid(uid);
    }

    @GetMapping("fetchhood")
    public @ResponseBody
    Neighborhood fetchHood(@RequestParam("uid") Integer uid) {
        return findHoodByUid(uid);
    }

    private List<Message> findAllMsg(Integer uid) {
        String sql = "Select mid as mid from message Where tid in \n"
            + "(Select tid From message Where message.timestamp > (Select lastlogouttime From user Where uid = ?) \n"
            + "and tid in (Select tid From threadparticipant natural join thread Where recid = ?))";

        List<Integer> mids = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            ps.setInt(2, uid);
            return ps;
        }, rs -> {
            mids.add(rs.getInt("mid"));
        });

        System.out.println(mids);
        List<Message> ret = messageDao.findAllById(mids);

        return ret;
    }

    private Block findBlockByUid(Integer uid) {
        String sql = "select bid from joinblock where uid = ?";
        AtomicInteger bid = new AtomicInteger();

        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            return ps;
        }, rs -> {
            bid.set(rs.getInt("bid"));
        });

        Block block = blockDao.findById(bid.get()).get();
        return block;
    }

    private Neighborhood findHoodByUid(Integer uid) {
        String sql = "SELECT nid from neighborhood WHERE nid = (SELECT nid from joinblock NATURAL join block where uid = ?)";

        AtomicInteger nid = new AtomicInteger();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            return ps;
        }, rs -> {
            nid.set(rs.getInt("nid"));
        });

        Neighborhood neighborhood = neighborhoodDao.findById(nid.get()).get();
        return neighborhood;
    }

}
