package com.teamx.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.Contest;

public interface ContestRepository extends MongoRepository<Contest, String> {
}