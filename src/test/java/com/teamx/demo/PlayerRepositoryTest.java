package com.teamx.demo;

import com.teamx.demo.model.Player;
import com.teamx.demo.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
    }

    @Test
    void testFindByMatchId() {
        Player player = new Player();
        player.setMatchId("match1");
        playerRepository.save(player);

        List<Player> players = playerRepository.findByMatchId("match1");

        assertThat(players).hasSize(1);
    }

    @Test
    void testFindByMatchIdIn() {
        Player player1 = new Player();
        player1.setMatchId("match1");
        Player player2 = new Player();
        player2.setMatchId("match2");
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players = playerRepository.findByMatchIdIn(List.of("match1", "match2"));

        assertThat(players).hasSize(2);
    }
}