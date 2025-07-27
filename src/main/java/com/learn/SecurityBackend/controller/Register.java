package com.learn.SecurityBackend.controller;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin("*")
public class Register {

    @Autowired
    RegisterService registerService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Login login){
        String token = registerService.loginUser(login);
        Map<String, String> res = new HashMap<>();
        res.put("token",token);
        return ResponseEntity.ok(res);
       }

//       @GetMapping("/login")
//       public String login(){
//        return "Login";
//       }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody Login login){
        login.setRole("User");
        registerService.addUser(login);
        return new ResponseEntity<>("User created",HttpStatus.CREATED);
    }
}
