package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMqConfig.QUEUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMqListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE, concurrency = "2-3")
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers){
//        var sendorId = temperatureLogData.getSensorId();
//        var temperature = temperatureLogData.getValue();
//        log.info("Temperature Listener: SensorId {} Temp {}",sendorId, temperature);
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));

    }




}
