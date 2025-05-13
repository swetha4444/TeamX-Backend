package com.teamx.demo;

import java.util.List;
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
    void testSignupLoginFailAndSuccessAndDeleteUser() {
        // Signup new user
        LoginModel signup = new LoginModel();
        signup.setEmail("testuser@example.com");
        signup.setPassword("testpass");
        signup.setUsername("testuser");
        HttpEntity<LoginModel> signupReq = new HttpEntity<>(signup);
        ResponseEntity<String> signupResp = restTemplate.postForEntity("/auth/signup", signupReq, String.class);

        // Accept both success (JSON) and error (plain text)
        if (signupResp.getStatusCode().is2xxSuccessful()) {
            // Optionally, try parsing JSON if you want
            assertThat(signupResp.getBody()).contains("email");
        } else if (signupResp.getStatusCode().is4xxClientError()) {
            // assertThat(signupResp.getBody()).contains("Email already exists");
            assertThat(signupResp.getBody()).contains("Email already registered");
        }

        // Login fail (wrong password)
        LoginModel loginFail = new LoginModel();
        loginFail.setEmail("testuser@example.com");
        loginFail.setPassword("wrongpass");
        HttpEntity<LoginModel> loginFailReq = new HttpEntity<>(loginFail);
        ResponseEntity<String> loginFailResp = restTemplate.postForEntity("/auth/login", loginFailReq, String.class);
        assertThat(loginFailResp.getStatusCode().is4xxClientError()).isTrue();
        assertThat(loginFailResp.getBody()).contains("Incorrect");

        // Login success
        LoginModel login = new LoginModel();
        login.setEmail("testuser@example.com");
        login.setPassword("testpass");
        HttpEntity<LoginModel> loginReq = new HttpEntity<>(login);
        ResponseEntity<Map> loginResp = restTemplate.postForEntity("/auth/login", loginReq, Map.class);
        assertThat(loginResp.getStatusCode().is2xxSuccessful()).isTrue();

        // Add to wallet
        ResponseEntity<Map> addWalletResp = restTemplate.postForEntity("/auth/wallet/add?email=testuser@example.com&amount=10", null, Map.class);
        assertThat(addWalletResp.getStatusCode().is2xxSuccessful()).isTrue();

        // Get wallet
        ResponseEntity<Map> walletResp = restTemplate.getForEntity("/auth/wallet?email=testuser@example.com", Map.class);
        assertThat(walletResp.getStatusCode().is2xxSuccessful()).isTrue();

        // Deduct from wallet
        HttpEntity<Map<String, Object>> deductReq = new HttpEntity<>(Map.of("email", "testuser@example.com", "amount", 5));
        ResponseEntity<Map> deductResp = restTemplate.postForEntity("/auth/deduct", deductReq, Map.class);
        assertThat(deductResp.getStatusCode().is2xxSuccessful()).isTrue();

        // Delete user
        ResponseEntity<Map> delResp = restTemplate.exchange("/auth/delete?email=testuser@example.com", HttpMethod.DELETE, null, Map.class);
        assertThat(delResp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void testLoginWithExistingUser() {
        // Existing user: new@example.com, password: new
        LoginModel login = new LoginModel();
        login.setEmail("new@example.com");
        login.setPassword("new");
        HttpEntity<LoginModel> loginReq = new HttpEntity<>(login);
        ResponseEntity<Map> loginResp = restTemplate.postForEntity("/auth/login", loginReq, Map.class);
        assertThat(loginResp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void testLoginWithWrongPassword() {
        // Existing user: new@example.com, wrong password
        LoginModel login = new LoginModel();
        login.setEmail("new@example.com");
        login.setPassword("wrongpassword");
        HttpEntity<LoginModel> loginReq = new HttpEntity<>(login);
        ResponseEntity<String> loginResp = restTemplate.postForEntity("/auth/login", loginReq, String.class);
        assertThat(loginResp.getStatusCode().is4xxClientError()).isTrue();
        assertThat(loginResp.getBody()).contains("Incorrect");
    }

    // --- MongoPingController ---
    @Test
    void testMongoPing() {
        ResponseEntity<String> response = restTemplate.getForEntity("/mongo-ping", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("MongoDB connection");
    }

    // --- ContestController ---
    @Test
    void testGetAllContests() {
        ResponseEntity<String> response = restTemplate.getForEntity("/contests", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    // --- PlayerController ---
    @Test
    void testGetPlayersByMatchId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/players/match/dummyMatchId", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testGetPlayersByMatchIds() {
        HttpEntity<List<String>> req = new HttpEntity<>(List.of("dummyMatchId"));
        ResponseEntity<String> response = restTemplate.postForEntity("/players/matches", req, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }

    // --- PointRecordController ---
    @Test
    void testGetAllPoints() {
        ResponseEntity<String> response = restTemplate.getForEntity("/points", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    // --- TeamController ---
    @Test
    void testGetTeamsByContestId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/contest/dummyContestId", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testGetTeamsByUserEmail() {
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/user/dummy@example.com", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }
}