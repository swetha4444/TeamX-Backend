package com.teamx.demo.model;

import java.util.List;

public class ContestWithTeams {
    private Contest contest;
    private List<Team> teams;

    public ContestWithTeams() {
    }

    public ContestWithTeams(Contest contest, List<Team> teams) {
        this.contest = contest;
        this.teams = teams;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}