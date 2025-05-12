package com.teamx.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamx.demo.model.Team;
import com.teamx.demo.repository.TeamRepository;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
    
    public List<Team> getTeamsByContestId(String contestId) {
        return teamRepository.findByContestId(contestId);
    }
    
    public List<Team> getTeamsByUserEmail(String userEmail) {
        return teamRepository.findByUserEmail(userEmail);
    }
    
    public Optional<Team> getTeamById(String teamId) {
        return teamRepository.findById(teamId);
    }
}