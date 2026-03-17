package com.example.demo1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class controller {

    @GetMapping("/")
    public String helloWorld() {
        return "hello lab1 (/info)";
    }

    @GetMapping("/info")
    public String info() {
        return "information you know";
    }
}
