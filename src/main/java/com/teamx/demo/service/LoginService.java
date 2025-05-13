package com.teamx.demo.service;

import com.teamx.demo.model.LoginModel;
import com.teamx.demo.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public List<LoginModel> getAllLogins() {
        return loginRepository.findAll();
    }

    public LoginModel saveLogin(LoginModel loginModel) {
        return loginRepository.save(loginModel);
    }

    public Optional<LoginModel> findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    public Optional<LoginModel> login(String email, String password) {
        Optional<LoginModel> userOpt = loginRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public void deleteByEmail(String email) {
        loginRepository.deleteByEmail(email);
    }
}