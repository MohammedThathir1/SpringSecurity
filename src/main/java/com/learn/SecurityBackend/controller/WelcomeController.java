package com.learn.SecurityBackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class WelcomeController {

    @RequestMapping("/welcome")
    public String greet(){
        return "Hello...";
    }
}
