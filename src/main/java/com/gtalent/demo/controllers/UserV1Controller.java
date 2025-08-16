package com.gtalent.demo.controllers;

import com.gtalent.demo.model.User;
import com.gtalent.demo.responses.CreateUserRequest;
import com.gtalent.demo.responses.GetUserResponse;
import com.gtalent.demo.responses.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/v1/users")
@CrossOrigin("*")
public class UserV1Controller {

//    下面由annotation
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

//    由建構子注入，比較建議按照這個方式注入，因為測試性、迴護性較佳
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserV1Controller(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public ResponseEntity<CreateUserRequest> createUser(@RequestBody CreateUserRequest request){
        String sql = "insert into users (username, email) values (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, request.getUsername(), request.getEmail());
        if (rowsAffected > 0) {
            CreateUserRequest response = new CreateUserRequest(request.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        //user序列化回傳Jason
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAllUsers(){
        String sql = "select id, username, email from users";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUsersById(@PathVariable int id){
        try{
            String sql = "select id, username, email from users where id = ?";
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
            return ResponseEntity.status(HttpStatus.OK).body(new GetUserResponse(user));
        } catch ( EmptyResultDataAccessException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserRequest> UpdateUsersById(@PathVariable int id, @RequestBody UpdateUserRequest request){
        String sql = "Update users set username=? where id=?";
        int rowsAffected = jdbcTemplate.update(sql,request.getUsername(), id);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(new UpdateUserRequest(request.getUsername()));
        }
            return ResponseEntity.notFound().build();
        }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id){
        String sql = "delete from users where id =?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search") //@RequestParam -> ?keyword=...
    public ResponseEntity<List<GetUserResponse>> searchUser (@RequestParam String keyword){
        String sql = "select * from users where username like ?";
        String searchKeyword = "%" + keyword + "%";
        List<User> users = jdbcTemplate.query(sql,new Object[]{searchKeyword}, new BeanPropertyRowMapper<>(User.class));
        // Kermit的 List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        // List <GetUserResponse> results = users.
        //                stream(). //lambda起手式
        //                filter(user -> user.getUsername().
        //                toLowerCase().contains(keyword.toLowerCase())).//找出符合keyword的
        //                map (GetUserResponse::new).
        //                //map( user -> this.toGetUserRequest(user))
        //                toList();

        List<GetUserResponse> responses =
                users.stream().
                map(user -> new GetUserResponse(user.getId(), user.getUsername())).toList();
        return new ResponseEntity<> (responses, HttpStatus.OK);
    }
}
