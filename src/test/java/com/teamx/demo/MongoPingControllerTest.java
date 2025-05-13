package com.teamx.demo;
import static org.assertj.core.api.Assertions.assertThat;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.teamx.demo.controller.MongoPingController;

class MongoPingControllerTest {

    @Test
    void testPingMongo_DoubleOk() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Document doc = new Document("ok", 1.0d);
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);

        MongoPingController controller = new MongoPingController();
        controller.mongoTemplate = mongoTemplate;
        String result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection is OK!");
    }

    @Test
    void testPingMongo_IntegerOk() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Document doc = new Document("ok", 1);
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);

        MongoPingController controller = new MongoPingController();
        controller.mongoTemplate = mongoTemplate;
        String result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection is OK!");
    }

    @Test
    void testPingMongo_DoubleNotOk() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Document doc = new Document("ok", 0.0d);
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);

        MongoPingController controller = new MongoPingController();
        controller.mongoTemplate = mongoTemplate;
        String result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection failed!");
    }

    @Test
    void testPingMongo_IntegerNotOk() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Document doc = new Document("ok", 0);
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);

        MongoPingController controller = new MongoPingController();
        controller.mongoTemplate = mongoTemplate;
        String result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection failed!");
    }

    @Test
    void testPingMongo_OkMissingOrOtherType() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Document doc = new Document(); // no "ok" field
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);

        MongoPingController controller = new MongoPingController();
        controller.mongoTemplate = mongoTemplate;
        String result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection failed!");

        // Test with a non-numeric "ok" value
        doc = new Document("ok", "notANumber");
        when(mongoTemplate.executeCommand(any(Document.class))).thenReturn(doc);
        result = controller.pingMongo();
        assertThat(result).isEqualTo("MongoDB connection failed!");
    }
}