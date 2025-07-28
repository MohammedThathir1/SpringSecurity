package com.learn.SecurityBackend.service;

import com.learn.SecurityBackend.entity.Login;
import com.learn.SecurityBackend.repo.WelcomeRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WelcomeRepo welcomeRepo;

    public UserDetailsServiceImpl(WelcomeRepo welcomeRepo) {
        this.welcomeRepo = welcomeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Login login = welcomeRepo.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

        return  new org.springframework.security.core.userdetails.User(
                login.getUsername(), login.getPassword(), login.getAuthorities());
//        return User.builder()
//                .username(login.getUsername())
//                .password(login.getPassword())
//                .roles(login.getRole())
//                .build();
    }
}
