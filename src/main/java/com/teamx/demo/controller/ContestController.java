package com.teamx.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.Contest;
import com.teamx.demo.repository.PlayerRepository;
import com.teamx.demo.repository.PointRecordRepository;
import com.teamx.demo.repository.TeamRepository;
import com.teamx.demo.service.ContestService;

@RestController
@RequestMapping("/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public List<Contest> getAllContests() {
        return contestService.getAllContests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContestById(@PathVariable String id) {
        return contestService.getContestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/join")
    public ResponseEntity<Contest> joinContest(@PathVariable String id) {
        return contestService.updatePrizePoolAndSpots(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContestAndRelated(@PathVariable String id) {
        // Delete contest
        contestService.getContestById(id).ifPresent(contest -> contestService.deleteById(id));

        // Delete all players with match_id = contest id
        playerRepository.deleteByMatchId(id);

        // Delete all points with contest_id = contest id
        pointRecordRepository.deleteByContestId(id);

        // Delete all teams with matchId = contest id
        teamRepository.deleteByContestId(id);

        return ResponseEntity.ok(
            Map.of("message", "Contest, related players, teams, and points deleted successfully")
        );
    }
}