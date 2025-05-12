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
    private Player captain;
    private Player viceCaptain;

    public Player getCaptain() {
        return captain;
    }

    public void setCaptain(Player captain) {
        this.captain = captain;
    }

    public Player getViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(Player viceCaptain) {
        this.viceCaptain = viceCaptain;
    }

    public Team() {
    }


    public Team(String id, String userEmail, String contestId, List<Player> players, Player captain,
            Player viceCaptain) {
        this.id = id;
        this.userEmail = userEmail;
        this.contestId = contestId;
        this.players = players;
        this.captain = captain;
        this.viceCaptain = viceCaptain;
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