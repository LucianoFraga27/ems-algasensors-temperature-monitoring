package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
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

    @RabbitListener(queues = QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers){
        var sendorId = temperatureLogData.getSensorId();
        var temperature = temperatureLogData.getValue();
        log.info("Headers: {}", headers.toString());
        log.info("Temperature Updated: SensorId {} Temp {}",sendorId, temperature);
        Thread.sleep(Duration.ofSeconds(5));
    }




}
