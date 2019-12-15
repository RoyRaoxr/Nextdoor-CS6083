package edu.nyu.cs6083.nextdoor.controller;


import edu.nyu.cs6083.nextdoor.bean.Message;
import edu.nyu.cs6083.nextdoor.bean.User;
import edu.nyu.cs6083.nextdoor.dao.MessageDao;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    @Autowired
    MessageDao messageDao;

    @PostMapping("search")
    public String search(@RequestParam Map<String, String> data, Model m,
        HttpServletRequest request) {
        var user = (User) request.getSession().getAttribute("useradmin");
        String keyword = data.get("keyword");
        List<Message> ms = messageDao.listByKey(user.getUid(), keyword);

        m.addAttribute("key", keyword);
        m.addAttribute("ms", ms);
        return "main/search";
    }
}
