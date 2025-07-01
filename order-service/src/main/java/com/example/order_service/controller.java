package com.example.order_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class controller {

    @GetMapping
    protected String hello() {
        return "Hello welcome to ORDERS";
    }
}
