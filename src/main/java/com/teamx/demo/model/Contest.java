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
            private String shortname;
            private String img;
            // getters and setters
        }
    }
}