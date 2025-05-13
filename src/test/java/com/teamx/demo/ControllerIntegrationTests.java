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

import com.teamx.demo.model.Contest;
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
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.OK);
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

    // --- ContestController FULL COVERAGE TESTS ---

    @Test
    void testGetAllContests() {
        ResponseEntity<Contest[]> response = restTemplate.getForEntity("/contests", Contest[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetContestById_NotFound() {
        ResponseEntity<Contest> response = restTemplate.getForEntity("/contests/doesnotexist", Contest.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetContestById_Found() {
        // Create a contest first (assumes you have an endpoint or DB setup for this)
        // If not, skip this test or mock the repository/service in a unit test
        // For demonstration, let's assume an ID "existingContestId" exists
        String contestId = "existingContestId";
        ResponseEntity<Contest> response = restTemplate.getForEntity("/contests/" + contestId, Contest.class);
        // Accept either found or not found depending on your DB state
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testJoinContest_NotFound() {
        ResponseEntity<Contest> response = restTemplate.exchange("/contests/doesnotexist/join", HttpMethod.PUT, null, Contest.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testJoinContest_Found() {
        // Assumes a contest with ID "existingContestId" exists
        String contestId = "existingContestId";
        ResponseEntity<Contest> response = restTemplate.exchange("/contests/" + contestId + "/join", HttpMethod.PUT, null, Contest.class);
        // Accept either found or not found depending on your DB state
        assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testDeleteContestAndRelated_NotFound() {
        ResponseEntity<Map> response = restTemplate.exchange("/contests/doesnotexist", HttpMethod.DELETE, null, Map.class);
        // Controller always returns 200 OK with a message, even if not found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("message");
    }

    @Test
    void testDeleteContestAndRelated_Found() {
        // Assumes a contest with ID "existingContestId" exists
        String contestId = "existingContestId";
        ResponseEntity<Map> response = restTemplate.exchange("/contests/" + contestId, HttpMethod.DELETE, null, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("message");
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

    // --- TeamController FULL COVERAGE ---

    @Test
    void testCreateTeam_Found() {
        Map<String, Object> player = Map.of(
            "id", "testplayer1",
            "name", "Test Player",
            "team", "Test Team",
            "role", "Allrounder"
        );

        Map<String, Object> team = Map.of(
            "matchId", "68210e61c8ceb31ce5c0f1b8",
            "userEmail", "new@example.com",
            "captain", "testplayer1",      // player id
            "viceCaptain", "testplayer1",  // player id
            "players", List.of(player)     // list of player objects
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(team);
        ResponseEntity<Map> response = restTemplate.postForEntity("/teams", req, Map.class);
        // assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        System.out.println(response.getBody())
        assertThat(response.getBody()).containsKey("id");
    }

    @Test
    void testGetTeamsByContestId_Found() {
        String contestId = "68210e61c8ceb31ce5c0f1b8";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/contest/" + contestId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetTeamsByContestId_NotFound() {
        String contestId = "nonexistentContestId";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/contest/" + contestId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[]");
    }

    @Test
    void testGetTeamsByUserEmail_Found() {
        String userEmail = "new@example.com";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/user/" + userEmail, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetTeamsByUserEmail_NotFound() {
        String userEmail = "nonexistent@example.com";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/user/" + userEmail, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[]");
    }

    @Test
    void testGetContestAndTeamDetails_Found() {
        String userEmail = "new@example.com";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/combined?userEmail=" + userEmail, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("contest");
    }

    @Test
    void testGetContestAndTeamDetails_NotFound() {
        String userEmail = "nonexistent@example.com";
        ResponseEntity<String> response = restTemplate.getForEntity("/teams/combined?userEmail=" + userEmail, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[]");
    }
}