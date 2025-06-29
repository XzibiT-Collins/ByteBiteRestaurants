package com.example.auth_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
