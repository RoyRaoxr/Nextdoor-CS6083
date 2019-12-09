package edu.nyu.cs6083.nextdoor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    
}
