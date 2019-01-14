package com.sornemus.gethatchtest.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

@Service
public class DeviceMetricSender
{
    private static final String DEVICE_NAME = "deviceone";

    @Autowired
    public MqttPahoClientFactory mqttClientFactory;

    //todo multiple methods - to imitate several different devices
    //todo send JSON as message text - wrap all info in it
    @Bean
    public IntegrationFlow mqttOutFlow()
    {

        return IntegrationFlows.from(
                new TemperatureMetricSource(),
                "getMetric",
                e -> e.poller(Pollers.fixedDelay(1000))
            )
            .handle(
                mqttOutbound()
            )
            .get();
    }

    //todo default topic should be device name, received from environment variable (?)
    @Bean
    public MessageHandler mqttOutbound()
    {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
            "iotDeviceSimulator",
            this.mqttClientFactory
        );

        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(DeviceMetricSender.DEVICE_NAME);
        messageHandler.setLoggingEnabled(true);
        messageHandler.setShouldTrack(true);
        return messageHandler;
    }
}
