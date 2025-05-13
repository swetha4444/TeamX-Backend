package com.teamx.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.Team;

public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findByContestId(String contestId);
    List<Team> findByUserEmail(String userEmail);
    void deleteByContestId(String contestId);
}