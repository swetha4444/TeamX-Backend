package com.teamx.demo;

import com.teamx.demo.model.PointRecord;
import com.teamx.demo.repository.PointRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class PointRecordRepositoryTest {

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @BeforeEach
    void setUp() {
        pointRecordRepository.deleteAll();
    }

    @Test
    void testFindByContestId() {
        PointRecord record = new PointRecord();
        record.setContestId("contest1");
        pointRecordRepository.save(record);

        Optional<PointRecord> result = pointRecordRepository.findByContestId("contest1");

        assertThat(result).isPresent();
        assertThat(result.get().getContestId()).isEqualTo("contest1");
    }
}