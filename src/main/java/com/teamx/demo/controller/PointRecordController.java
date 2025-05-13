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
 * REST controller for managing point records for fantasy contests.
 * <p>
 * This controller provides endpoints to:
 * <ul>
 *   <li>Retrieve all point records</li>
 *   <li>Retrieve points for a specific contest</li>
 *   <li>Create a new point record</li>
 *   <li>Update an existing point record</li>
 *   <li>Delete a point record</li>
 * </ul>
 * Each point record is associated with a contest and contains a list of points earned by players or teams.
 * </p>
 */
@RestController
@RequestMapping("/points")
public class PointRecordController {
    @Autowired
    private PointRecordService pointRecordService;

    /**
     * Retrieves all point records in the system.
     *
     * @return a list of all {@link PointRecord} objects stored in the database.
     *         Each record contains the contest ID and the list of points for that contest.
     */
    @GetMapping
    public List<PointRecord> getAll() {
        return pointRecordService.getAll();
    }

    /**
     * Retrieves the point record for a specific contest.
     *
     * @param contestId the unique identifier of the contest whose points are to be fetched.
     * @return a {@link ResponseEntity} containing the {@link PointRecord} if found,
     *         or a 404 Not Found response if no record exists for the given contest ID.
     */
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<PointRecord> getByContestId(@PathVariable String contestId) {
        return pointRecordService.getByContestId(contestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new point record for a contest.
     *
     * @param record the {@link PointRecord} object to be created. Must include the contest ID and initial points.
     * @return the created {@link PointRecord} with its generated ID.
     */
    @PostMapping
    public PointRecord create(@RequestBody PointRecord record) {
        return pointRecordService.save(record);
    }

    /**
     * Updates an existing point record by its ID.
     *
     * @param id     the unique identifier of the point record to update.
     * @param record the updated {@link PointRecord} object. The ID in the path will override the ID in the body.
     * @return a {@link ResponseEntity} containing the updated {@link PointRecord}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointRecord> update(@PathVariable String id, @RequestBody PointRecord record) {
        record.setId(id);
        return ResponseEntity.ok(pointRecordService.save(record));
    }

    /**
     * Deletes a point record by its unique ID.
     *
     * @param id the unique identifier of the point record to delete.
     * @return a {@link ResponseEntity} with HTTP 204 No Content if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pointRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}