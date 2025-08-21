package com.gtalent.demo.service;


import com.gtalent.demo.controllers.AuthResponse;
import com.gtalent.demo.model.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.responses.LoginRequest;
import com.gtalent.demo.responses.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        //  1.建立User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPwd(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        // 2. 產出token
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse auth(LoginRequest request) {
        // 1. 找到對應的User
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if (request.getPassword().equals(user.getPwd())){
                //2. 產出token
                String jwtToken = jwtService.generateToken(user);
                return new AuthResponse(jwtToken);
            }
        }
        throw new RuntimeException("無效的憑證");
    }
}
