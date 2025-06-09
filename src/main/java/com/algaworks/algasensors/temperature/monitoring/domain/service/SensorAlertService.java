package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        var sensorId = new SensorId(temperatureLogData.getSensorId());
        sensorAlertRepository.findById(sensorId).ifPresentOrElse(alert -> {

            if (alert.getMinTemperature() != null &&
                    temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                log.info("Alerting Max Temp SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());

            } else if (alert.getMinTemperature() != null &&
                    temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                log.info("Alerting Min Temp SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());

            } else {
                log.info("Alerting Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
            }

        }, () ->
                log.info("Alerting Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue()));
    }

}

