package com.teamx.demo;

import com.teamx.demo.model.LoginModel;
import com.teamx.demo.repository.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class LoginRepositoryTest {

    @Autowired
    private LoginRepository loginRepository;

    @BeforeEach
    void setUp() {
        loginRepository.deleteAll();
    }

    @Test
    void testFindByEmail() {
        LoginModel login = new LoginModel();
        login.setEmail("test@example.com");
        loginRepository.save(login);

        Optional<LoginModel> result = loginRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testDeleteByEmail() {
        LoginModel login = new LoginModel();
        login.setEmail("test@example.com");
        loginRepository.save(login);

        loginRepository.deleteByEmail("test@example.com");

        assertThat(loginRepository.findByEmail("test@example.com")).isEmpty();
    }
}