package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public SensorMonitoringOutput getDetail(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefaultConfig(sensorId);
        return SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId().getValue())
                .lastTemperature(sensorMonitoring.getLastTemperature())
                .enabled(sensorMonitoring.getEnabled())
                .updatedAt(sensorMonitoring.getUpdatedAt())
                .build();
    }

    private SensorMonitoring findByIdOrDefaultConfig(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updatedAt(null)
                        .build());
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable (@PathVariable TSID sensorId) {
       var sensor = findByIdOrDefaultConfig(sensorId);
       if (Boolean.TRUE.equals(sensor.getEnabled())) {
           throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
       }
        sensor.setEnabled(true);
        sensorMonitoringRepository.saveAndFlush(sensor);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SneakyThrows
    public void disable (@PathVariable TSID sensorId) {
        var sensor = findByIdOrDefaultConfig(sensorId);
        if (Boolean.FALSE.equals(sensor.getEnabled())) {
            Thread.sleep(Duration.ofSeconds(10));
            // throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        sensor.setEnabled(false);
        sensorMonitoringRepository.saveAndFlush(sensor);
    }


}
