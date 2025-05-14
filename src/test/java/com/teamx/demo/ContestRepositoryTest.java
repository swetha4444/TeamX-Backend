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
    }

    @Test
    void testFindById() {
        Contest contest = new Contest();
        contest.setId("contest1");
        contestRepository.save(contest);

        assertThat(contestRepository.findById("contest1")).isPresent();
    }

    @Test
    void testDeleteById() {
        Contest contest = new Contest();
        contest.setId("contest1");
        contestRepository.save(contest);

        contestRepository.deleteById("contest1");

        assertThat(contestRepository.findById("contest1")).isEmpty();
    }
}