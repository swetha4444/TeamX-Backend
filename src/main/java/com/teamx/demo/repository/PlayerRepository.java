package com.teamx.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamx.demo.model.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByMatchId(String match_id);
    List<Player> findByMatchIdIn(List<String> matchIds);
}