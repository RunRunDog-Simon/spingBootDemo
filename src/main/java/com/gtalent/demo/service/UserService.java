package com.gtalent.demo.service;

import com.gtalent.demo.model.User;
import com.gtalent.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsernameAndPassword(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && user.get().getPwd().equals(password)){
            return user;
        }
        return Optional.empty();
    }
}
