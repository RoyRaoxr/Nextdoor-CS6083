package edu.nyu.cs6083.nextdoor.controller;

import edu.nyu.cs6083.nextdoor.bean.Message;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.MessageDao;
import edu.nyu.cs6083.nextdoor.dao.ThreadDao;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MessageDao messageDao;

    @Autowired
    ThreadDao threadDao;

    @GetMapping("reply")
    public String reply(HttpServletRequest request, Model m, @RequestParam("id") Integer mid) {
        User user = (User) request.getSession().getAttribute("useradmin");
        Message replyMsg = messageDao.findById(mid).get();

        String sql = "select mid from message where mid != ? and tid = ?";

        List<Integer> mids = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mid);
            ps.setInt(2, replyMsg.getMsgThread().getTid());
            return ps;
        }, rs -> {
            mids.add(rs.getInt("mid"));
        });
        List<Message> sameThreadMsg = messageDao.findAllById(mids);

        m.addAttribute("msg", replyMsg);
        m.addAttribute("all", sameThreadMsg);
        return "main/reply";
    }

    @PostMapping("replymsg")
    public @ResponseBody
    String replyMsg(@RequestParam Map<String, String> map) {
        String sql =
            "Insert into message (`tid`, `author`, `title`, `timestamp`, `text`, `replyid`, `lat`,`lng`) "
                + "Values (?, ?, ?, now(), ?, ?,?, ?)";

        Integer tid = Integer.valueOf(map.get("tid"));
        String title = map.get("title");
        int replymid = Integer.valueOf(map.get("replymid"));
        Integer author = Integer.valueOf(map.get("author"));
        String text = map.get("content");

        if (!map.containsKey("lat")) {
            jdbcTemplate.update(sql, tid, author, title, text, replymid, 0, 0);
        } else {
            Double lat = Double.valueOf(map.get("lat"));
            Double lng = Double.valueOf(map.get("lng"));
            jdbcTemplate
                .update(sql, tid, author, title, text, replymid, map.get("lat"), map.get("lng"));
        }

        return "redirect:message";
    }

    @GetMapping("message")
    public String message(HttpServletRequest request, Model m) {
        User user = (User) request.getSession().getAttribute("useradmin");

        //initial message mid
        String sql = "Select mid as mid from message Where tid in \n"
            + "(Select tid From threadparticipant natural join thread Where recid = ? and type = ?)";

        //type 3 block
        List<Integer> type3 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 3);
            return ps;
        }, rs -> {
            type3.add(rs.getInt("mid"));
        });
        List<Message> type3s = messageDao.findAllById(type3);

        // type 2 hood
        List<Integer> type2 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 2);
            return ps;
        }, rs -> {
            type2.add(rs.getInt("mid"));
        });
        List<Message> type2s = messageDao.findAllById(type2);

//
        //type 1 friends
        List<Integer> type1 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 1);
            return ps;
        }, rs -> {
            type1.add(rs.getInt("mid"));
        });
        List<Message> type1s = messageDao.findAllById(type1);

        //type 0 neighbors
        List<Integer> type0 = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUid());
            ps.setInt(2, 0);
            return ps;
        }, rs -> {
            type0.add(rs.getInt("mid"));
        });
        List<Message> type0s = messageDao.findAllById(type0);

        m.addAttribute("t3", type3s);
        m.addAttribute("t2", type2s);
        m.addAttribute("t1", type1s);
        m.addAttribute("t0", type0s);
        return "main/message";
    }

    @PostMapping("/sendmessage")
    public String send(@RequestParam Map<String, String> data, HttpServletRequest request) {
        String state = data.get("select");
        User user = (User) request.getSession().getAttribute("useradmin");
        System.out.println(data);
        switch (state) {
            case "toneighbor":
                insertMessage(0, data, user);
                break;
            case "tofriend":
                insertMessage(1, data, user);
                break;
            case "allblock":
                insertMessage(3, data, user);
                break;
            case "allhood":
                insertMessage(2, data, user);
                break;
            case "allfriend":
                String sql = "select friendid from friends where userid = ?";
                List<Integer> fids = new ArrayList<>();
                jdbcTemplate.query(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, user.getUid());
                    return ps;
                }, rs -> {
                    fids.add(rs.getInt("friendid"));
                });

                String s1 = "INSERT INTO `nextdoor`.`thread`(`subject`, `type`) VALUES (?, ?);";
                String s2 =
                    "Insert into message (`tid`, `author`, `title`, `timestamp`, `text`, `lat`,`lng`) "
                        + "Values (?, ?, ?, now(), ?, ?, ?)";
                String s3 = "Insert into threadparticipant values(?, ?)";

                jdbcTemplate.update(s1, data.get("title"), 1);

                int tid = jdbcTemplate
                    .queryForObject(
                        "select tid from thread where tid = (select max(tid) from thread)",
                        Integer.class);

                jdbcTemplate
                    .update(s2, tid, user.getUid(), data.get("title"), data.get("content"), data.get("lat"), data.get("lng"));
                for (int id : fids) {
                    jdbcTemplate.update(s3, tid, id);
                }
                break;
        }

        return "redirect:main";
    }

    private void insertMessage(int type, Map<String, String> data, User user) {
        String s1 = "INSERT INTO `nextdoor`.`thread`(`subject`, `type`) VALUES (?, ?);";
        String s2 =
            "Insert into message (`tid`, `author`, `title`, `timestamp`, `text`, `lat`,`lng`) "
                + "Values (?, ?, ?, now(), ?, ?, ?)";
        String s3 = "Insert into threadparticipant values(?, ?)";

        int recvid = Integer.MIN_VALUE;

        jdbcTemplate.update(s1, data.get("title"), type);

        int tid = jdbcTemplate
            .queryForObject("select tid from thread where tid = (select max(tid) from thread)",
                Integer.class);

        jdbcTemplate.update(s2, tid, user.getUid(), data.get("title"), data.get("content"), data.get("lat"), data.get("lng"));

        if (type == 1) {
            recvid = Integer.valueOf(data.get("tofriend"));
            jdbcTemplate.update(s3, tid, recvid);
        } else if (type == 0) {
            recvid = Integer.valueOf(data.get("toneighbor"));
            jdbcTemplate.update(s3, tid, recvid);
        } else if (type == 2) {
            String s5 = "select uid from joinblock natural join block "
                + "where nid = (select nid from joinblock natural join block where uid = ?)";

            String s6 = "SELECT bid from block where nid = (select nid from joinblock natural join block where uid = ?)";
            String s7 = "INSERT INTO `nextdoor`.`threadblock`(`tid`, `bid`) VALUES (?,?)";

            List<Integer> bids = new ArrayList<>();
            jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(s6);
                ps.setInt(1, user.getUid());
                return ps;
            }, rs -> {
                bids.add(rs.getInt("bid"));
            });

            for (int id : bids) {
                jdbcTemplate.update(s7, tid, id);
            }

            List<Integer> ids = new ArrayList<>();
            jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(s5);
                ps.setInt(1, user.getUid());
                return ps;
            }, rs -> {
                ids.add(rs.getInt("uid"));
            });

            for (int id : ids) {
                jdbcTemplate.update(s3, tid, id);
            }

        } else if (type == 3) {
            String s4 = "select uid from joinblock where bid = (select bid from joinblock where uid = ?)";
            String s5 = "select bid from joinblock where uid = ?";
            String s6 = "INSERT INTO `nextdoor`.`threadblock`(`tid`, `bid`) VALUES (?,?)";

            AtomicInteger bid = new AtomicInteger();
            jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(s5);
                ps.setInt(1, user.getUid());
                return ps;
            }, rs -> {
                bid.set(rs.getInt("bid"));
            });

            List<Integer> ids = new ArrayList<>();

            jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(s4);
                ps.setInt(1, user.getUid());
                return ps;
            }, rs -> {
                ids.add(rs.getInt("uid"));
            });

            for (int id : ids) {
                jdbcTemplate.update(s3, tid, id);
            }

            jdbcTemplate.update(s6, tid, bid.get());
        }


    }
}
