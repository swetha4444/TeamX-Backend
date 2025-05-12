package com.teamx.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.Contest;
import com.teamx.demo.model.ContestWithTeams;
import com.teamx.demo.model.Team;
import com.teamx.demo.service.ContestService;
import com.teamx.demo.service.TeamService;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;
    
    @Autowired
    private ContestService contestService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Team team) {
        Team savedTeam = teamService.createTeam(team);
        // Reduce spots left for the contest associated with the team.
        contestService.reduceSpotsLeft(team.getContestId());
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
        if (teamOpt.isPresent()) {
            return ResponseEntity.ok(teamOpt.get().getCaptain());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
    }
    
    @GetMapping("/{teamId}/viceCaptain")
    public ResponseEntity<?> getTeamViceCaptain(@PathVariable String teamId) {
        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if (teamOpt.isPresent()) {
            return ResponseEntity.ok(teamOpt.get().getViceCaptain());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
    }
    
    // New combined endpoint using ContestWithTeams model
    @GetMapping("/combined")
    public ResponseEntity<?> getContestAndTeamDetails(@RequestParam String userEmail) {
        // Get all teams created by the user.
        List<Team> userTeams = teamService.getTeamsByUserEmail(userEmail);
        
        // Collect unique contestIds from the user's teams.
        Set<String> contestIds = userTeams.stream()
                .map(Team::getContestId)
                .collect(Collectors.toSet());
        
        // For each contestId, get contest details and attach teams for the contest.
        List<ContestWithTeams> combinedResponse = new ArrayList<>();
        for (String contestId : contestIds) {
            Optional<Contest> contestOpt = contestService.getContestById(contestId);
            if (contestOpt.isPresent()) {
                Contest contest = contestOpt.get();
                List<Team> teamsForContest = teamService.getTeamsByContestId(contestId);
                combinedResponse.add(new ContestWithTeams(contest, teamsForContest));
            }
        }
        return ResponseEntity.ok(combinedResponse);
    }
}