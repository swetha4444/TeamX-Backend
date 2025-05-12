package com.teamx.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "players")
public class Player {
    @Id
    private String id;
    private String name;
    private String role;
    private String battingStyle;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getBattingStyle() {
        return battingStyle;
    }
    public void setBattingStyle(String battingStyle) {
        this.battingStyle = battingStyle;
    }
    public String getBowlingStyle() {
        return bowlingStyle;
    }
    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPlayerImg() {
        return playerImg;
    }
    public void setPlayerImg(String playerImg) {
        this.playerImg = playerImg;
    }
    public Integer getCredit() {
        return credit;
    }
    public void setCredit(Integer credit) {
        this.credit = credit;
    }
    private String bowlingStyle;
    private String country;
    private String playerImg;
    private Integer credit;
    @Field("match_id")
    private String matchId;

    // Getters and setters
    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
    // ...other getters/setters...
}