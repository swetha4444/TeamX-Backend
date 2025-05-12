package com.teamx.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamx.demo.model.Player;
import com.teamx.demo.repository.PlayerRepository;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getPlayersByMatchId(String matchId) {
        return playerRepository.findByMatchId(matchId);
    }

    public List<Player> getPlayersByMatchIds(List<String> matchIds) {
        return playerRepository.findByMatchIdIn(matchIds);
    }

    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }
}