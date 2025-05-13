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
    
    @Test
    void testUpdatePrizePoolAndSpots() {
        Contest.ContestData data = new Contest.ContestData();
        data.setPrizePool(100);
        data.setEntryFee(10);
        data.setSpotsLeft(2);
        Contest contest = new Contest();
        contest.setData(List.of(data));
        when(contestRepository.findById("id")).thenReturn(Optional.of(contest));
        when(contestRepository.save(any())).thenReturn(contest);

        Optional<Contest> result = contestService.updatePrizePoolAndSpots("id");
        assertThat(result).isPresent();
        assertThat(data.getPrizePool()).isEqualTo(110);
        assertThat(data.getSpotsLeft()).isEqualTo(1);

        // No data
        Contest contestNoData = new Contest();
        contestNoData.setData(List.of());
        when(contestRepository.findById("noData")).thenReturn(Optional.of(contestNoData));
        assertThat(contestService.updatePrizePoolAndSpots("noData")).isPresent();

        // Not found
        when(contestRepository.findById("none")).thenReturn(Optional.empty());
        assertThat(contestService.updatePrizePoolAndSpots("none")).isEmpty();
    }

        @Test
    void testReduceSpotsLeft() {
        Contest.ContestData data = new Contest.ContestData();
        data.setSpotsLeft(2);
        Contest contest = new Contest();
        contest.setData(List.of(data));
        when(contestRepository.findById("id")).thenReturn(Optional.of(contest));
        when(contestRepository.save(any())).thenReturn(contest);

        Optional<Contest> result = contestService.reduceSpotsLeft("id");
        assertThat(result).isPresent();
        assertThat(data.getSpotsLeft()).isEqualTo(1);

        // No data
        Contest contestNoData = new Contest();
        contestNoData.setData(List.of());
        when(contestRepository.findById("noData")).thenReturn(Optional.of(contestNoData));
        assertThat(contestService.reduceSpotsLeft("noData")).isPresent();

        // Not found
        when(contestRepository.findById("none")).thenReturn(Optional.empty());
        assertThat(contestService.reduceSpotsLeft("none")).isEmpty();
    }

    @Test
    void testDeleteById() {
        contestService.deleteById("id");
        verify(contestRepository).deleteById("id");
    }
}