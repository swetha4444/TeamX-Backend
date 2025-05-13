package com.teamx.demo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.teamx.demo.model.PointRecord;
import com.teamx.demo.repository.PointRecordRepository;
import com.teamx.demo.service.PointRecordService;

class PointRecordServiceTest {

    @Mock
    PointRecordRepository pointRecordRepository;

    @InjectMocks
    PointRecordService pointRecordService;

    PointRecordServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(pointRecordRepository.findAll()).thenReturn(List.of(new PointRecord()));
        assertThat(pointRecordService.getAll()).hasSize(1);
    }

}