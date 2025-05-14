package com.teamx.demo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.teamx.demo.model.LoginModel;
import com.teamx.demo.repository.LoginRepository;
import com.teamx.demo.service.LoginService;

class LoginServiceTest {

    @Mock
    LoginRepository loginRepository;

    @InjectMocks
    LoginService loginService;

    LoginServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLogins() {
        when(loginRepository.findAll()).thenReturn(List.of(new LoginModel()));
        assertThat(loginService.getAllLogins()).hasSize(1);
    }

    @Test
    void testLogin2() {
        LoginModel login = new LoginModel();
        login.setPassword("pw");
        when(loginRepository.findByEmail("a")).thenReturn(Optional.of(login));
        assertThat(loginService.login("a", "pw")).contains(login); // password matches
        assertThat(loginService.login("a", "wrong")).isEmpty();    // password does not match

        when(loginRepository.findByEmail("b")).thenReturn(Optional.empty());
        assertThat(loginService.login("b", "pw")).isEmpty();       // user not found
    }

    @Test
    void testSaveLogin() {
        LoginModel login = new LoginModel();
        when(loginRepository.save(login)).thenReturn(login);
        assertThat(loginService.saveLogin(login)).isEqualTo(login);
    }
}