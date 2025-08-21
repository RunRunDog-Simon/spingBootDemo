package com.gtalent.demo.controllers;

import com.gtalent.demo.model.User;
import com.gtalent.demo.responses.CreateUserRequest;
import com.gtalent.demo.responses.GetUserResponse;
import com.gtalent.demo.responses.UpdateUserRequest;
import com.gtalent.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin("*")
public class UserV2Controller {

    private final UserRepository userRepository;

    @Autowired
    public UserV2Controller(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUsersById(@PathVariable int id){
//        下面是我原本寫的
//        try{
//            User user = userRepository.getReferenceById(id);
//            return ResponseEntity.status(HttpStatus.OK).body(new GetUserResponse(user));
//        } catch ( EmptyResultDataAccessException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            GetUserResponse response = new GetUserResponse(user.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserRequest> UpdateUsersById(@PathVariable int id, @RequestBody UpdateUserRequest request){
////        int rowsAffected = userRepository.
//        if (userRepository.existsById(id)) {
//            User user = userRepository.getReferenceById(id);
//            user.setUsername(request.getUsername());
////            user.setEmail(request.setUsername() 不允許修改email
//            return ResponseEntity.ok(new UpadteUserRequest(request.getUsername()));
//        }
//        return ResponseEntity.notFound().build();
        //解題思路: 1. 先找到user 2. 拿出user 3. set user(UpdateUser裡只允set username)
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User updatedUser = user.get();
            System.out.println("Before Update:" + updatedUser);

            updatedUser.setUsername(request.getUsername());
            System.out.println("Before Save:" + updatedUser);

//          User savedUser = userRepository.save(updatedUser);  之前我為何這樣打?
            updatedUser = userRepository.save(updatedUser);
            UpdateUserRequest response = new UpdateUserRequest(updatedUser.getUsername());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id){

        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GetUserResponse> createUsers(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        User savedUser = userRepository.save(user);
        GetUserResponse response = new GetUserResponse(savedUser);
        return ResponseEntity.ok(response);
    }

//    Kermit的
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUsersById(@PathVariable int id) {
//        Optional<User> user = userRepository.findById(id);
//        userRepository.delete(user.get());
//        return ResponseEntity.noContent().build();
//    }

}
