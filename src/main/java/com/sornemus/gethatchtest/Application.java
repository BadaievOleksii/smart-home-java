package com.sornemus.gethatchtest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.messaging.MessageHandler;

@SpringBootApplication
public class Application {



    public static void main(final String... args)
    {
//        final String USAGE = "\n" +
//            "Usage:\n" +
//            "    CreateTable <table>\n\n" +
//            "Where:\n" +
//            "    table - the table to create.\n\n" +
//            "Example:\n" +
//            "    CreateTable GreetingsTable\n";
//
//        if (args.length < 1) {
//            System.out.println(USAGE);
//            System.exit(1);
//        }
//
//        /* Read the name from command args */
//        String table_name = args[0];
//
//
//        CreateTableRequest request = new CreateTableRequest()
//            .withAttributeDefinitions(
//                new AttributeDefinition("Language", ScalarAttributeType.S),
//                new AttributeDefinition("Greeting", ScalarAttributeType.S))
//            .withKeySchema(
//                new KeySchemaElement("Language", KeyType.HASH),
//                new KeySchemaElement("Greeting", KeyType.RANGE))
//            .withProvisionedThroughput(
//                new ProvisionedThroughput(new Long(10), new Long(10)))
//            .withTableName(table_name);
//
//        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
//
//        try {
//            CreateTableResult result = ddb.createTable(request);
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//            System.exit(1);
//        }
//        System.out.println("Done!");


        SpringApplication.run(Application.class, args);
    }



    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "tcp://localhost:1883" });
        options.setUserName("guest");
        options.setPassword("guest".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }


    @Bean
    public IntegrationFlow mqttOutFlow() {
        return IntegrationFlows.from(
                CharacterStreamReadingMessageSource.stdin(),
                e -> e.poller(Pollers.fixedDelay(1000))
            )
            .transform(p -> p + " sent to MQTT")
            .handle(
                mqttOutbound()
            )
            .get();
    }

    @Bean
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("iotDevicePublisher", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("devicename");
        messageHandler.setLoggingEnabled(true);
        messageHandler.setShouldTrack(true);
        return messageHandler;
    }


    private LoggingHandler logger() {
        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        loggingHandler.setLoggerName("gethatch");
        return loggingHandler;
    }




    // consumer

    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows
            .from(
                mqttInbound()
            )
            .handle(
                logger()
            )
            .get();
    }

    @Bean
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
            "iotDeviceConsumer",
            mqttClientFactory(),
            "devicename"//todo subscribe on "/device/*" (? - check wildcard format)
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }
}

