package com.learn.SecurityBackend.configuration;

import com.learn.SecurityBackend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Confi {

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

       http
               .csrf
                       (csrf -> csrf.disable())
               .authorizeHttpRequests
                       (
                               auth -> auth
                        .requestMatchers("/api/register/**")
                                       .permitAll()
                        .anyRequest().authenticated()
                        )
                    .httpBasic(Customizer.withDefaults());

           // http.authenticationProvider(authenticationProvider());

            return http.build();
        }

        @Autowired
        private final UserDetailsServiceImpl userDetailsService;  // Declare it here
       // Assuming you have this

    // Constructor injection
        public Confi(UserDetailsServiceImpl userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        protected void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(userDetailsService).
                    passwordEncoder(passwordEncoder());
        }


}
