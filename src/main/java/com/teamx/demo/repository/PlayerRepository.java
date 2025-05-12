package com.teamx.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByMatchId(String matchId);
    List<Player> findByMatchIdIn(List<String> matchIds);
}