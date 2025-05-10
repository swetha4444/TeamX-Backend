package com.teamx.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.LoginModel;
import com.teamx.demo.service.LoginService;
import com.teamx.demo.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public List<LoginModel> getAllLogins() {
        return loginService.getAllLogins();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginModel loginRequest) {
        Optional<LoginModel> userOpt = loginService.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not registered");
        }
        LoginModel user = userOpt.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }
        String token = JwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginModel signupRequest) {
        if (loginService.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }
        LoginModel savedUser = loginService.saveLogin(signupRequest);
        String token = JwtUtil.generateToken(savedUser.getEmail());
        return ResponseEntity.ok().body(token);
    }
}