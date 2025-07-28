package com.learn.SecurityBackend.controller;

import com.learn.SecurityBackend.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {

    @Autowired
    private OauthService oauthService;

    @GetMapping("/google")
    public ResponseEntity<?> oauthGoogle(@RequestParam String code){
        //String code = body.get("code");
        System.out.println("Received code: " + code);
        return oauthService.oauthGoogle(code);
    }
    @GetMapping("/github")
    public ResponseEntity<?> oauthGithub(@RequestParam String code){

        System.out.println("Received code: " + code);
        return oauthService.oauthGithub(code);
    }
}
