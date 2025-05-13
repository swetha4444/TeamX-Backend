package com.teamx.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.LoginModel;
import com.teamx.demo.service.LoginService;
import com.teamx.demo.utils.JwtUtil;

/**
 * REST controller for authentication and wallet operations.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Returns all login records.
     * @return list of all users
     */
    @GetMapping
    public List<LoginModel> getAllLogins() {
        return loginService.getAllLogins();
    }

    /**
     * Authenticates a user and returns a JWT token if successful.
     * @param loginRequest the login request containing email and password
     * @return JWT token and user info if successful, error otherwise
     */
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
        return ResponseEntity.ok().body(
            Map.of(
                "jwt", token,
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
            )
        );
    }

    /**
     * Registers a new user.
     * @param signupRequest the signup request containing user details
     * @return JWT token and user info if successful, error otherwise
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginModel signupRequest) {
        if (loginService.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }
        signupRequest.setWallet(100); // Ensure wallet is initialized
        LoginModel savedUser = loginService.saveLogin(signupRequest);
        String token = JwtUtil.generateToken(savedUser.getEmail());
        return ResponseEntity.ok().body(
            Map.of(
                "jwt", token,
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "wallet", savedUser.getWallet()
            )
        );
    }

    /**
     * Deducts an amount from the user's wallet.
     * @param payload map containing "email" and "amount"
     * @return updated wallet balance or error
     */
    @PostMapping("/deduct")
    public ResponseEntity<?> deductFromWallet(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        int amount = (int) payload.get("amount");
        Optional<LoginModel> userOpt = loginService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        LoginModel user = userOpt.get();
        if (amount > user.getWallet()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient wallet balance");
        }
        user.setWallet(user.getWallet() - amount);
        loginService.saveLogin(user);
        return ResponseEntity.ok(Map.of(
            "message", "Deduction successful",
            "wallet", user.getWallet()
        ));
    }

    /**
     * Adds money to the user's wallet.
     * @param email the user's email
     * @param amount the amount to add
     * @return updated wallet balance or error
     */
    @PostMapping("/wallet/add")
    public ResponseEntity<?> addToWallet(@RequestParam String email, @RequestParam int amount) {
        Optional<LoginModel> userOpt = loginService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        LoginModel user = userOpt.get();
        user.setWallet(user.getWallet() + amount);
        loginService.saveLogin(user);
        return ResponseEntity.ok(Map.of(
            "message", "Amount added successfully",
            "wallet", user.getWallet()
        ));
    }

    /**
     * Gets wallet details for a user.
     * @param email the user's email
     * @return wallet balance or error
     */
    @GetMapping("/wallet")
    public ResponseEntity<?> getWalletDetails(@RequestParam String email) {
        Optional<LoginModel> userOpt = loginService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        LoginModel user = userOpt.get();
        return ResponseEntity.ok(Map.of(
            "email", user.getEmail(),
            "wallet", user.getWallet()
        ));
    }

    /**
     * Deletes a user by email.
     * @param email the user's email
     * @return success or error message
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String email) {
        Optional<LoginModel> userOpt = loginService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        loginService.deleteByEmail(email);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}