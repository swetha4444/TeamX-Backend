package com.teamx.demo.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to check MongoDB connectivity.
 */
@RestController
public class MongoPingController {

    @Autowired
    public MongoTemplate mongoTemplate;

    /**
     * Endpoint to ping MongoDB and verify connection.
     * @return a message indicating if MongoDB connection is OK or failed
     */
    @GetMapping("/mongo-ping")
    public String pingMongo() {
        Document ping = new Document("ping", 1);
        Document result = mongoTemplate.executeCommand(ping);
        Object okValue = result.get("ok");
        if ((okValue instanceof Double && ((Double) okValue) == 1.0) ||
            (okValue instanceof Integer && ((Integer) okValue) == 1)) {
            return "MongoDB connection is OK!";
        } else {
            return "MongoDB connection failed!";
        }
    }
}