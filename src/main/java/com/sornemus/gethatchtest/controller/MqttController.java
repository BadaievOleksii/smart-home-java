package com.sornemus.gethatchtest.controller;

import org.eclipse.paho.client.mqttv3.util.Debug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttController {

    private final MqttPahoClientFactory mqttClientFactory;

    @Autowired
    public MqttController(MqttPahoClientFactory mqttClientFactory) {
        this.mqttClientFactory = mqttClientFactory;
    }

    @RequestMapping(
        "/iot/publish"
    )
    public String publishToMqtt() {
        return "Hello Docker World";
    }
}
