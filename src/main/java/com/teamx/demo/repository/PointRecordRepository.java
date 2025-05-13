package com.teamx.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.PointRecord;

public interface PointRecordRepository extends MongoRepository<PointRecord, String> {
    Optional<PointRecord> findByContestId(String contestId);
    void deleteByContestId(String contestId);
}