package com.learn.SecurityBackend.service;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WelcomeService {

    @Autowired
    private WelcomeRepo repo;

    public ResponseEntity<List<Login>> getData() {
        return  new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
}
