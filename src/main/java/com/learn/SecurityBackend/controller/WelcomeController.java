package com.learn.SecurityBackend.controller;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    @Autowired
    private WelcomeService service;

    @GetMapping("/welcome")
    public ResponseEntity<List<Login>> greet(){
        return service.getData();
    }
}
