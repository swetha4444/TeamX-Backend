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

    @Test
    void testGetPlayersByMatchIds() {
        when(playerRepository.findByMatchIdIn(List.of("m1", "m2"))).thenReturn(List.of(new Player()));
        assertThat(playerService.getPlayersByMatchIds(List.of("m1", "m2"))).hasSize(1);
    }
 
    @Test
    void testGetPlayerById() {
        Player player = new Player();
        when(playerRepository.findById("id")).thenReturn(Optional.of(player));
        assertThat(playerService.getPlayerById("id")).contains(player);

        when(playerRepository.findById("none")).thenReturn(Optional.empty());
        assertThat(playerService.getPlayerById("none")).isEmpty();
    }
}