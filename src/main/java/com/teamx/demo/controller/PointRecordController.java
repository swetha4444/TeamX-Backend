package com.teamx.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamx.demo.model.PointRecord;
import com.teamx.demo.service.PointRecordService;

/**
 * REST controller for managing point records.
 * Provides endpoints to retrieve, create, update, and delete points for contests.
 */
@RestController
@RequestMapping("/points")
public class PointRecordController {
    @Autowired
    private PointRecordService pointRecordService;

    /**
     * Retrieves all point records.
     * @return list of all point records
     */
    @GetMapping
    public List<PointRecord> getAll() {
        return pointRecordService.getAll();
    }

    /**
     * Retrieves the point record for a specific contest.
     * @param contestId the contest ID
     * @return the point record if found, or 404 if not found
     */
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<PointRecord> getByContestId(@PathVariable String contestId) {
        return pointRecordService.getByContestId(contestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new point record.
     * @param record the point record to create
     * @return the created point record
     */
    @PostMapping
    public PointRecord create(@RequestBody PointRecord record) {
        return pointRecordService.save(record);
    }

    /**
     * Updates an existing point record by ID.
     * @param id the point record ID
     * @param record the updated point record
     * @return the updated point record
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointRecord> update(@PathVariable String id, @RequestBody PointRecord record) {
        record.setId(id);
        return ResponseEntity.ok(pointRecordService.save(record));
    }

    /**
     * Deletes a point record by ID.
     * @param id the point record ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pointRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}