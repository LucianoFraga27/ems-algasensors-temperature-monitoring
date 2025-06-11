package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        log.info("processTemperatureReading");
        if (temperatureLogData.getValue().equals(10.5)){
            throw new RuntimeException("Erro forçado");
        }

        var sensorId = new SensorId(temperatureLogData.getSensorId());
        sensorMonitoringRepository.findById(sensorId).ifPresentOrElse(
                sensor -> {
                    if (sensor.isEnable()) {

                        sensor.setLastTemperature(temperatureLogData.getValue());
                        sensor.setUpdatedAt(OffsetDateTime.now());
                        sensorMonitoringRepository.saveAndFlush(sensor);

                        TemperatureLog temperatureLog = TemperatureLog.builder()
                                .id(new TemperatureLogId(temperatureLogData.getId()))
                                .registeredAt(temperatureLogData.getRegisteredAt())
                                .value(temperatureLogData.getValue())
                                .sensorId(sensorId)
                                .build();
                        temperatureLogRepository.save(temperatureLog);
                        log.info("Temperature Updated: SensorId {} Temp {}", sensorId, temperatureLogData.getValue());
                    } else {
                        log.info("Temperature Ignored: SensorId {} Temp {}", sensorId, temperatureLogData.getValue());
                    }
                },
                () -> log.info("Temperature Ignored: SensorId {} Temp {}", sensorId, temperatureLogData.getValue()));

    }

}
