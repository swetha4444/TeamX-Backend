package com.teamx.demo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.teamx.demo.model.Contest;
import com.teamx.demo.repository.ContestRepository;
import com.teamx.demo.service.ContestService;

class ContestServiceTest {

    @Mock
    ContestRepository contestRepository;

    @InjectMocks
    ContestService contestService;

    ContestServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllContests() {
        when(contestRepository.findAll()).thenReturn(List.of(new Contest()));
        assertThat(contestService.getAllContests()).hasSize(1);
    }

    @Test
    void testGetContestById() {
        Contest contest = new Contest();
        when(contestRepository.findById("id")).thenReturn(Optional.of(contest));
        assertThat(contestService.getContestById("id")).contains(contest);

        when(contestRepository.findById("none")).thenReturn(Optional.empty());
        assertThat(contestService.getContestById("none")).isEmpty();
    }
}