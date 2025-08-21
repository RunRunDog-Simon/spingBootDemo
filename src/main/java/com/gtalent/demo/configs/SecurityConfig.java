package com.gtalent.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //stateless jwt 用不上CSRF
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/products/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()

                                .requestMatchers(HttpMethod.POST, "/v2/users/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/v2/users/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/v2/users/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/suppliers/**").hasRole("SUPPLIER")
                                .requestMatchers(HttpMethod.PUT, "/suppliers/**").hasRole("SUPPLIER")
                                .requestMatchers(HttpMethod.DELETE, "/suppliers/**").hasRole("SUPPLIER")
                                .anyRequest().permitAll()
//                        .anyRequest().authenticated()<--只保留這邊，就是全部攔截的意思，但不可能這樣，因為會連登入都不行
                )
                //restful 核心: 伺服器無法從Session終獲獲得使用者資訊
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 確保spring security filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
