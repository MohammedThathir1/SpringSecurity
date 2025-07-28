package com.learn.SecurityBackend.service;


import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class OauthService {

   // private static final Logger log = LoggerFactory.getLogger(OauthService.class);

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${GITHUB_CLIENT_ID}")
    private String githubclientId;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String githubclientSecret;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WelcomeRepo welcomeRepo;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> oauthGoogle(String code) {
        try {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "http://localhost:5173/oauth-success");
           // params.add("redirect_uri", "https://developers.google.com/oauthplayground");
            params.add("grant_type", "authorization_code");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            if(userInfoResponse.getStatusCode()== HttpStatus.OK){
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try{
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (Exception e){
                    Login login = new Login();
                    login.setUsername(email);
                    login.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    login.setRole("User");
                    welcomeRepo.save(login);
                }
                String jwtToken = jwtService.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token",jwtToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    public ResponseEntity<?> oauthGithub(String code) {
        try {
            // Step 1: Exchange code for access token
            String tokenEndpoint = "https://github.com/login/oauth/access_token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", githubclientId);
            params.add("client_secret", githubclientSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);

            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to get access token");
            }

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            // Step 2: Fetch user info using the access token
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);
            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<Map> userResponse = restTemplate.exchange(
                    "https://api.github.com/user",
                    HttpMethod.GET,
                    userRequest,
                    Map.class
            );

            Map<String, Object> userInfo = userResponse.getBody();
            String email = (String) userInfo.get("email");

            // GitHub may not return email in /user, fetch separately
            if (email == null || email.isEmpty()) {
                ResponseEntity<List> emailResponse = restTemplate.exchange(
                        "https://api.github.com/user/emails",
                        HttpMethod.GET,
                        userRequest,
                        List.class
                );
                List<Map<String, Object>> emails = emailResponse.getBody();
                for (Map<String, Object> e : emails) {
                    if (Boolean.TRUE.equals(e.get("primary"))) {
                        email = (String) e.get("email");
                        break;
                    }
                }
            }

            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email not found from GitHub");
            }

            // Step 3: Check user and generate JWT
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(email);
            } catch (Exception e) {
                Login login = new Login();
                login.setUsername(email);
                login.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                login.setRole("User");
                welcomeRepo.save(login);
            }

            String jwtToken = jwtService.generateToken(email);
            return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
