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

@RestController
@RequestMapping("/points")
public class PointRecordController {
    @Autowired
    private PointRecordService pointRecordService;

    @GetMapping
    public List<PointRecord> getAll() {
        return pointRecordService.getAll();
    }

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<PointRecord> getByContestId(@PathVariable String contestId) {
        return pointRecordService.getByContestId(contestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PointRecord create(@RequestBody PointRecord record) {
        return pointRecordService.save(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointRecord> update(@PathVariable String id, @RequestBody PointRecord record) {
        record.setId(id);
        return ResponseEntity.ok(pointRecordService.save(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pointRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}