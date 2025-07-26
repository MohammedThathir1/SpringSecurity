package com.learn.SecurityBackend.service;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private WelcomeRepo welcomeRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void addUser(Login login) {

        login.setPassword(passwordEncoder.encode(login.getPassword()));
        Login save = welcomeRepo.save(login);
        //return new ResponseEntity<>(save,HttpStatus.CREATED);
    }

    public String loginUser(Login login) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken
                                (login.getUsername(),login.getPassword()));

        if(authentication.isAuthenticated()) return jwtService.generateToken(login.getUsername());

        return "fail";
    }
}
