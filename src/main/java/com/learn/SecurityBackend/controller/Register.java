package com.learn.SecurityBackend.controller;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class Register {

    @Autowired
    RegisterService registerService;

    @GetMapping("/login")
    public String setUser(){
        return "Enter User details";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody Login login){
        registerService.addUser(login);
        return new ResponseEntity<>("User created",HttpStatus.CREATED);
    }
}
