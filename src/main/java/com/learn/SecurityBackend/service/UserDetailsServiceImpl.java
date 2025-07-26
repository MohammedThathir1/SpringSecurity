package com.learn.SecurityBackend.service;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WelcomeRepo welcomeRepo;

    public UserDetailsServiceImpl(WelcomeRepo welcomeRepo) {
        this.welcomeRepo = welcomeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        return welcomeRepo.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

//        return User.builder()
//                .username(login.getUsername())
//                .password(login.getPassword())
//                .roles(login.getRole())
//                .build();
    }
}
