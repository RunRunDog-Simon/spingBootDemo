package com.gtalent.demo.controllers;

import com.gtalent.demo.model.User;
import com.gtalent.demo.responses.LoginRequest;
import com.gtalent.demo.responses.RegisterRequest;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/session")
@CrossOrigin("*")
public class SessionAuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    //這邊很偷懶，實務不准

    @Autowired
    public SessionAuthController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository; //這邊很偷懶，實務不准
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request, HttpSession session) {
        System.out.println("開始註冊，密碼設置請包含一個大寫以及大於等於12個字元並小於等於20個字元，且皆不可含有空格");
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            System.out.println("使用者名稱已存在");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            System.out.println("信箱已被使用");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (!request.getEmail().contains("@")){
            System.out.println("信箱內容不符合格式，請使用@寫出正確信箱名稱");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()){
            System.out.println("密碼不可為空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (request.getPassword().length()<12 || request.getPassword().length()>20){
            System.out.println("密碼長度不對，請大於等於12個字元並小於等於20個字元");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!request.getPassword().matches(".*[A-Z].*")){
            System.out.println("密碼請包涵至少一個大寫");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPwd(request.getPassword().trim());
        User savedUser = userRepository.save(user);
        session.setAttribute("userId" , user.getId());
        //此處代表登入後，直接保留登入狀態，不用重新登入，現代網站常這樣做。push測試
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request, HttpSession session){
        Optional<User> user = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(user.isPresent()){
            session.setAttribute("userId", user.get().getId());
        return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userRepository.findById(userId);
        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
