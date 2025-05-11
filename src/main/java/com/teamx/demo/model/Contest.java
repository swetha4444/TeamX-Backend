package com.teamx.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contests")
public class Contest {
    @Id
    private String id;
    private List<ContestData> data;

    // getters and setters

    public static class ContestData {
        private String name;
        private String matchType;
        private String status;
        private String venue;
        private String date;

        public ContestData() {
            // Default constructor
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVenue() {
            return venue;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDateTimeGMT() {
            return dateTimeGMT;
        }

        public void setDateTimeGMT(String dateTimeGMT) {
            this.dateTimeGMT = dateTimeGMT;
        }

        public List<String> getTeams() {
            return teams;
        }

        public void setTeams(List<String> teams) {
            this.teams = teams;
        }

        public List<TeamInfo> getTeamInfo() {
            return teamInfo;
        }

        public void setTeamInfo(List<TeamInfo> teamInfo) {
            this.teamInfo = teamInfo;
        }

        public String getSeries_id() {
            return series_id;
        }

        public void setSeries_id(String series_id) {
            this.series_id = series_id;
        }

        public boolean isFantasyEnabled() {
            return fantasyEnabled;
        }

        public void setFantasyEnabled(boolean fantasyEnabled) {
            this.fantasyEnabled = fantasyEnabled;
        }

        public boolean isBbbEnabled() {
            return bbbEnabled;
        }

        public void setBbbEnabled(boolean bbbEnabled) {
            this.bbbEnabled = bbbEnabled;
        }

        public boolean isHasSquad() {
            return hasSquad;
        }

        public void setHasSquad(boolean hasSquad) {
            this.hasSquad = hasSquad;
        }

        public boolean isMatchStarted() {
            return matchStarted;
        }

        public void setMatchStarted(boolean matchStarted) {
            this.matchStarted = matchStarted;
        }

        public boolean isMatchEnded() {
            return matchEnded;
        }

        public void setMatchEnded(boolean matchEnded) {
            this.matchEnded = matchEnded;
        }

        public int getPrizePool() {
            return prizePool;
        }

        public void setPrizePool(int prizePool) {
            this.prizePool = prizePool;
        }

        public int getEntryFee() {
            return entryFee;
        }

        public void setEntryFee(int entryFee) {
            this.entryFee = entryFee;
        }

        public int getTotalSpots() {
            return totalSpots;
        }

        public void setTotalSpots(int totalSpots) {
            this.totalSpots = totalSpots;
        }

        public int getSpotsLeft() {
            return spotsLeft;
        }

        public void setSpotsLeft(int spotsLeft) {
            this.spotsLeft = spotsLeft;
        }

        private String dateTimeGMT;
        private List<String> teams;
        private List<TeamInfo> teamInfo;
        private String series_id;
        private boolean fantasyEnabled;
        private boolean bbbEnabled;
        private boolean hasSquad;
        private boolean matchStarted;
        private boolean matchEnded;
        private int prizePool;
        private int entryFee;
        private int totalSpots;
        private int spotsLeft;

        // getters and setters

        public static class TeamInfo {
            private String name;
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
            public String getShortname() {
                return shortname;
            }
            public void setShortname(String shortname) {
                this.shortname = shortname;
            }
            public String getImg() {
                return img;
            }
            public void setImg(String img) {
                this.img = img;
            }
            private String shortname;
            private String img;
            // getters and setters
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ContestData> getData() {
        return data;
    }

    public void setData(List<ContestData> data) {
        this.data = data;
    }
}