package com.teamx.demo;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.teamx.demo.model.LoginModel;

/**
 * Integration tests for all REST controllers.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    // --- LoginController ---
    @Test
    void testGetAllLogins() {
        ResponseEntity<String> response = restTemplate.getForEntity("/auth", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testLoginWithNonExistentEmail() {
        LoginModel login = new LoginModel();
        login.setEmail("doesnotexist@example.com");
        login.setPassword("irrelevant");
        HttpEntity<LoginModel> loginReq = new HttpEntity<>(login);
        ResponseEntity<String> loginResp = restTemplate.postForEntity("/auth/login", loginReq, String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(loginResp.getBody()).contains("not registered");
    }

    @Test
    void testSignupWithExistingEmail() {
        LoginModel signup = new LoginModel();
        signup.setEmail("new@example.com"); // already exists
        signup.setPassword("new");
        signup.setUsername("newuser");
        HttpEntity<LoginModel> signupReq = new HttpEntity<>(signup);
        ResponseEntity<String> signupResp = restTemplate.postForEntity("/auth/signup", signupReq, String.class);
        assertThat(signupResp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(signupResp.getBody()).contains("already registered");
    }

    @Test
    void testDeductFromWalletInsufficientFunds() {
        HttpEntity<Map<String, Object>> deductReq = new HttpEntity<>(Map.of("email", "new@example.com", "amount", 99999));
        ResponseEntity<String> deductResp = restTemplate.postForEntity("/auth/deduct", deductReq, String.class);
        assertThat(deductResp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(deductResp.getBody()).contains("Insufficient wallet balance");
    }

    @Test
    void testGetWalletForNonExistentUser() {
        ResponseEntity<String> walletResp = restTemplate.getForEntity("/auth/wallet?email=doesnotexist@example.com", String.class);
        assertThat(walletResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(walletResp.getBody()).contains("User not found");
    }

    @Test
    void testDeleteNonExistentUser() {
        ResponseEntity<String> delResp = restTemplate.exchange("/auth/delete?email=doesnotexist@example.com", HttpMethod.DELETE, null, String.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(delResp.getBody()).contains("User not found");
    }

    
}