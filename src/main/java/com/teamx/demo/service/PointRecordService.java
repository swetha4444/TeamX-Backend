package com.teamx.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamx.demo.model.PointRecord;
import com.teamx.demo.repository.PointRecordRepository;

@Service
public class PointRecordService {
    @Autowired
    private PointRecordRepository pointRecordRepository;

    public List<PointRecord> getAll() {
        return pointRecordRepository.findAll();
    }

    public Optional<PointRecord> getByContestId(String contestId) {
        return pointRecordRepository.findByContestId(contestId);
    }

    public PointRecord save(PointRecord record) {
        return pointRecordRepository.save(record);
    }

    public void deleteById(String id) {
        pointRecordRepository.deleteById(id);
    }
}