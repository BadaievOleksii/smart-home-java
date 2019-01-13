package com.sornemus.gethatchtest.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

@Service
public class DeviceMetricSender
{
    private static final String DEVICE_NAME = "deviceone";

    @Autowired
    public MqttPahoClientFactory mqttClientFactory;


    @Bean
    public IntegrationFlow mqttOutFlow()
    {

        return IntegrationFlows.from(
                new DeviceMetricSource(),
                "getMessageText",
                e -> e.poller(Pollers.fixedDelay(1000))
            )
            .handle(
                mqttOutbound()
            )
            .get();
    }

    @Bean
    public MessageHandler mqttOutbound()
    {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
            "iotDevicePublisher",
            this.mqttClientFactory
        );

        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(DeviceMetricSender.DEVICE_NAME);
        messageHandler.setLoggingEnabled(true);
        messageHandler.setShouldTrack(true);
        return messageHandler;
    }
}
