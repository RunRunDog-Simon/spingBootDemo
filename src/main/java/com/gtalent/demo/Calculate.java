package com.gtalent.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Scanner;

@RestController
public class Calculate {
    @GetMapping("/addint")
    public int addint(int a){
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        return 1+2+a;
    }
}
