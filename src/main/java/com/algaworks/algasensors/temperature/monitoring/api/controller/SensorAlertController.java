package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput get (@PathVariable TSID sensorId) {
        var sensor = sensorAlertRepository.findById(new SensorId(sensorId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return SensorAlertOutput.builder()
                .id(sensor.getId().getValue())
                .maxTemperature(sensor.getMaxTemperature())
                .minTemperature(sensor.getMinTemperature())
                .build();
    }

    @PutMapping
    public SensorAlertOutput put (@PathVariable TSID sensorId, @RequestBody SensorAlertInput input) {
        SensorAlert sensor = sensorAlertRepository.findById(new SensorId(sensorId)).orElse(
                SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .build()
        );
        sensor.setMinTemperature(input.getMinTemperature());
        sensor.setMaxTemperature(input.getMaxTemperature());
        sensor = sensorAlertRepository.saveAndFlush(sensor);
        return SensorAlertOutput.builder()
                .id(sensor.getId().getValue())
                .maxTemperature(sensor.getMaxTemperature())
                .minTemperature(sensor.getMinTemperature())
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable TSID sensorId) {
        var sensor = sensorAlertRepository.findById(new SensorId(sensorId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        sensorAlertRepository.delete(sensor);
    }







}
