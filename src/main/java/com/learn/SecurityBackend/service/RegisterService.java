package com.learn.SecurityBackend.service;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    WelcomeRepo welcomeRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void addUser(Login login) {

        login.setPassword(passwordEncoder.encode(login.getPassword()));
        Login save = welcomeRepo.save(login);
        //return new ResponseEntity<>(save,HttpStatus.CREATED);
    }
}
