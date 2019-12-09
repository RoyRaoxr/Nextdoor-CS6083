package edu.nyu.cs6083.nextdoor.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {


    @GetMapping("/getlocation")
    public String getLocation() {
        return "";
    }
}
