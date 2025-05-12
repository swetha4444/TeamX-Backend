package com.teamx.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.Player;
import com.teamx.demo.service.PlayerService;

@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/match/{matchId}")
    public List<Player> getPlayersByMatchId(@PathVariable String matchId) {
        return playerService.getPlayersByMatchId(matchId);
    }

    @PostMapping("/matches")
    public List<Player> getPlayersByMatchIds(@RequestBody List<String> matchIds) {
        return playerService.getPlayersByMatchIds(matchIds);
    }

    @GetMapping("/{id}")
    public Optional<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id);
    }
}