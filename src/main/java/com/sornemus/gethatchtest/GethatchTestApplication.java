package com.sornemus.gethatchtest;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import com.amazonaws.AmazonServiceException;

public class GethatchTestApplication {
    public static void main(String[] args)
    {
        final String USAGE = "\n" +
            "Usage:\n" +
            "    CreateTable <table>\n\n" +
            "Where:\n" +
            "    table - the table to create.\n\n" +
            "Example:\n" +
            "    CreateTable GreetingsTable\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        /* Read the name from command args */
        String table_name = args[0];


        CreateTableRequest request = new CreateTableRequest()
            .withAttributeDefinitions(
                new AttributeDefinition("Language", ScalarAttributeType.S),
                new AttributeDefinition("Greeting", ScalarAttributeType.S))
            .withKeySchema(
                new KeySchemaElement("Language", KeyType.HASH),
                new KeySchemaElement("Greeting", KeyType.RANGE))
            .withProvisionedThroughput(
                new ProvisionedThroughput(new Long(10), new Long(10)))
            .withTableName(table_name);

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        try {
            CreateTableResult result = ddb.createTable(request);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }

}

