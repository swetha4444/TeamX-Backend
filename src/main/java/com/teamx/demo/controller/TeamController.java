package com.teamx.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.teamx.demo.model.Team;
import com.teamx.demo.service.TeamService;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Team team) {
        Team savedTeam = teamService.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeam);
    }
    
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<?> getTeamsByContestId(@PathVariable String contestId) {
        List<Team> teams = teamService.getTeamsByContestId(contestId);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<?> getTeamsByUserEmail(@PathVariable String userEmail) {
        List<Team> teams = teamService.getTeamsByUserEmail(userEmail);
        return ResponseEntity.ok(teams);
    }
    
    @GetMapping("/{teamId}/captain")
    public ResponseEntity<?> getTeamCaptain(@PathVariable String teamId) {
        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if(teamOpt.isPresent()){
            return ResponseEntity.ok(teamOpt.get().getCaptain());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
    }
    
    @GetMapping("/{teamId}/viceCaptain")
    public ResponseEntity<?> getTeamViceCaptain(@PathVariable String teamId) {
        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if(teamOpt.isPresent()){
            return ResponseEntity.ok(teamOpt.get().getViceCaptain());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
    }
}