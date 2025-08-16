package com.gtalent.demo.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello () {
        return "Hello, World";
    }

    @GetMapping("/number")
    public int number (){
        return 123;
    }

    @PostMapping("/post")
    public String post(){
        return "post";
    }

    @PutMapping("/Put")
    public String put(){
        return "put";
    }


//    @GetMapping("/addint")
//    public int addint(int a){
//        Scanner scanner = new Scanner(System.in);
//        a = scanner.nextInt();
//        return 1+2+a;
//    }
}