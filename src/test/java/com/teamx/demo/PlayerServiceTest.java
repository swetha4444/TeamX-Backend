package com.teamx.demo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.teamx.demo.model.Player;
import com.teamx.demo.repository.PlayerRepository;
import com.teamx.demo.service.PlayerService;

class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService playerService;

    PlayerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPlayersByMatchId() {
        when(playerRepository.findByMatchId("m")).thenReturn(List.of(new Player()));
        assertThat(playerService.getPlayersByMatchId("m")).hasSize(1);
    }
}