package edu.nyu.cs6083.nextdoor.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlockController {


    @GetMapping("block")
    public String block() {
        return "main/block";
    }
}
