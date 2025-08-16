package com.gtalent.demo.controllers;

import com.gtalent.demo.model.User;
import com.gtalent.demo.responses.CreateUserRequest;
import com.gtalent.demo.responses.GetUserResponse;
import com.gtalent.demo.responses.UpdateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;



@RestController
@RequestMapping("/users")
public class UserController {
//    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> mockUser = new HashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger();


    //建構子:每當UserComtroller被讀取時要做的事情
    public UserController(){
        //假裝有一個資料庫
        mockUser.put(0, new User(0, "admin","admin@gmail.com"));
        mockUser.put(1, new User(1, "Jack","abc@gmail.com"));
        mockUser.put(2, new User(2, "Kitty","def@gmail.com"));
        mockUser.put(3, new User(3, "John","ghi@gmail.com"));
        mockUser.put(4, new User(4, "Baise","jkl@gmail.com"));
        mockUser.put(5, new User(5, "Green","mno@gmail.com"));
        atomicInteger.set(6);
    }
    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAlluser(){
        List<User> userList = new ArrayList<>(mockUser.values());
        List<GetUserResponse> responses = new ArrayList<>();
        for (User user: userList){
            GetUserResponse response = new GetUserResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id){
        User user = mockUser.get(id);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        GetUserResponse response = new GetUserResponse(user.getId(), user.getUsername());
//      上面這行也可寫成下面這樣
//        response.getId(user.getId());
//        response.getUsername(user.getUsername());
        return ResponseEntity.ok(response);
    }
    //除上面回傳值外，還可以用下面兩種回傳值
    //return new ResponseEntity<>(user, HttpStatus.CREATED)
    //return ResponseEntity.status(HttpStatus.OK).body(user)

    @PutMapping("{id}")
    public ResponseEntity<UpdateUserRequest> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request){
        User user = mockUser.get(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        System.out.println("暱稱更新為:" + request.getUsername());

        //update user with request body from postman
        user.setUsername(request.getUsername());
        UpdateUserRequest response = new UpdateUserRequest(user.getUsername());

        return new ResponseEntity<> (response, HttpStatus.OK);
    }
    @PostMapping("{id}")
    public ResponseEntity<User> createUser(@PathVariable int id, @RequestBody User request){
        User user = mockUser.get(id);
        if (user == null){
            System.out.println(request.getUsername());
            System.out.println(request.getEmail());
            User userTemp = new User(id,null,null);
            userTemp.setId(id);
            userTemp.setUsername(request.getUsername());
            userTemp.setEmail(request.getEmail());
            mockUser.put(id,new User(id,userTemp.getUsername(),userTemp.getEmail()));
            return ResponseEntity.ok(userTemp);
        }
        return ResponseEntity.ok(user);
    }

    //Kermit的
    //土法煉鋼:找到最大key後+1 這個沒有呈現
    @PostMapping
    public ResponseEntity<CreateUserRequest> createUser(@RequestBody CreateUserRequest request){
        int newId = atomicInteger.getAndIncrement();
        User user = new User(newId, request.getUsername(), request.getEmail());
        mockUser.put(newId, user);
        //username
        CreateUserRequest response = new CreateUserRequest(user.getUsername());
        //user序列化回傳jason
        return new ResponseEntity<> (response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUserByID (@PathVariable int id){
        User user = mockUser.get(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        System.out.println("刪除id為" + id + "的資料");
        mockUser.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetUserResponse>> searchUser (@RequestParam String keyword){
        List <GetUserResponse> results = mockUser.values().
                stream(). //lambda起手式
                filter(user -> user.getUsername().
                toLowerCase().contains(keyword.toLowerCase())).//找出符合keyword的
                map (GetUserResponse::new).
                //map( user -> this.toGetUserRequest(user))
                toList();
        List<GetUserResponse> responses = new ArrayList<>();

        return new ResponseEntity<> (responses, HttpStatus.OK);
    }

    @GetMapping("/normal")
    public ResponseEntity<List<GetUserResponse>> getNormal (){
        List <GetUserResponse> results = mockUser.values().
                stream().
                filter(user -> ! user.getUsername().
                toLowerCase().equals("admin")).
                map (GetUserResponse::new).
                toList();
        return new ResponseEntity<> (results, HttpStatus.OK);
    }

}
