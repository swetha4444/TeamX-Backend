package com.teamx.demo;

import com.teamx.demo.model.Contest;
import com.teamx.demo.repository.ContestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ContestRepositoryTest {

    @Autowired
    private ContestRepository contestRepository;

    @BeforeEach
    void setUp() {
        contestRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        Contest contest = new Contest();
        contestRepository.save(contest);

        List<Contest> contests = contestRepository.findAll();

        assertThat(contests).hasSize(1);
    }}