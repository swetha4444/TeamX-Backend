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

/**
 * REST controller for managing player entities.
 * Provides endpoints to retrieve players by match ID(s) or player ID.
 */
@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    /**
     * Retrieves all players for a given match ID.
     * @param matchId the match ID
     * @return list of players for the match
     */
    @GetMapping("/match/{matchId}")
    public List<Player> getPlayersByMatchId(@PathVariable String matchId) {
        return playerService.getPlayersByMatchId(matchId);
    }

    /**
     * Retrieves all players for a list of match IDs.
     * @param matchIds list of match IDs
     * @return list of players for the given matches
     */
    @PostMapping("/matches")
    public List<Player> getPlayersByMatchIds(@RequestBody List<String> matchIds) {
        return playerService.getPlayersByMatchIds(matchIds);
    }

    /**
     * Retrieves a player by their ID.
     * @param id the player ID
     * @return the player if found, or empty if not found
     */
    @GetMapping("/{id}")
    public Optional<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id);
    }
}