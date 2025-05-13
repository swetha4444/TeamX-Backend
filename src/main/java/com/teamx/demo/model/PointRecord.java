package com.teamx.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "points")
public class PointRecord {
    @Id
    private String id;

    @Field("contest_id")
    private String contestId;

    private List<Point> points;

    public static class Point {
        private String type;
        private int point;

        // getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public int getPoint() { return point; }
        public void setPoint(int point) { this.point = point; }
    }

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContestId() { return contestId; }
    public void setContestId(String contestId) { this.contestId = contestId; }

    public List<Point> getPoints() { return points; }
    public void setPoints(List<Point> points) { this.points = points; }
}