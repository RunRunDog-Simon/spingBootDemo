package com.gtalent.demo.controllers;

import com.gtalent.demo.responses.LoginRequest;
import com.gtalent.demo.responses.RegisterRequest;
import com.gtalent.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@CrossOrigin("*")
public class JwtAuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.auth(request));
    }
}
