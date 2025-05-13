package com.teamx.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.LoginModel;

public interface LoginRepository extends MongoRepository<LoginModel, String> {
    Optional<LoginModel> findByEmail(String email);
    void deleteByEmail(String email);
}