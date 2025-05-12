package com.teamx.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teams")
public class Team {
    @Id
    private String id;
    
    private String userEmail;
    private String contestId;
    private List<Player> players;

    public Team() {
    }

    public Team(String userEmail, String contestId, List<Player> players) {
        this.userEmail = userEmail;
        this.contestId = contestId;
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}