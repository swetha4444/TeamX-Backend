package com.teamx.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/sample")
    public String sampleEndpoint() {
        return "Hello, this is a sample endpoint!";
    }
}