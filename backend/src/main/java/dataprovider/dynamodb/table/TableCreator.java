package dataprovider.dynamodb.table;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;

public class TableCreator {

  private final AmazonDynamoDBClient dynamoDBClient;

  public TableCreator(AmazonDynamoDBClient dynamoDBClient) {
    this.dynamoDBClient = dynamoDBClient;
  }

  public void createIfNotExists() {
    ListTablesResult tables = dynamoDBClient.listTables();
    if (!tables.getTableNames().contains("User")) {
      createUserTable();
    }
    if (!tables.getTableNames().contains("Topic")) {
      createTopicTable();
    }
    if (!tables.getTableNames().contains("Vote")) {
      createVoteTable();
    }
  }

  public void recreateTables() {
    ListTablesResult tables = dynamoDBClient.listTables();
    System.out.println(tables);
    if (tables.getTableNames().contains("User")) {
      System.out.println("deleting table");
      dynamoDBClient.deleteTable("User");
      tables = dynamoDBClient.listTables();
      System.out.println(tables);
    }
    if (tables.getTableNames().contains("Topic")) {
      dynamoDBClient.deleteTable("Topic");
    }
    if (tables.getTableNames().contains("Vote")) {
      dynamoDBClient.deleteTable("Vote");
    }
    createUserTable();
    createTopicTable();
    createVoteTable();
  }

  private void createUserTable() {
    ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("userId")
        .withAttributeType("S"));
    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement()
        .withAttributeName("userId")
        .withKeyType(KeyType.HASH));
    CreateTableRequest request = new CreateTableRequest()
        .withTableName("User")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attributeDefinitions)
        .withProvisionedThroughput( new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1));
    dynamoDBClient.createTable(request);
  }

  private void createTopicTable() {
    ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("topicId")
        .withAttributeType("S"));
    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.HASH));
    CreateTableRequest request = new CreateTableRequest()
        .withTableName("Topic")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attributeDefinitions)
        .withProvisionedThroughput( new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1));
    dynamoDBClient.createTable(request);
  }

  private void createVoteTable() {
    ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("voteId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("topicId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("choiceId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("userId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("dateTime")
        .withAttributeType("S"));
    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement()
        .withAttributeName("voteId")
        .withKeyType(KeyType.HASH));

    GlobalSecondaryIndex topicChoiceIndex = new GlobalSecondaryIndex()
        .withIndexName("TopicChoiceIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY));

    ArrayList<KeySchemaElement> topicChoiceIndexKeySchema = new ArrayList<KeySchemaElement>();
    topicChoiceIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.HASH));
    topicChoiceIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("choiceId")
        .withKeyType(KeyType.RANGE));


    topicChoiceIndex.setKeySchema(topicChoiceIndexKeySchema);

    GlobalSecondaryIndex userIndex = new GlobalSecondaryIndex()
        .withIndexName("userIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY));

    ArrayList<KeySchemaElement> userIndexKeySchema = new ArrayList<KeySchemaElement>();
    userIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("userId")
        .withKeyType(KeyType.HASH));
    userIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("dateTime")
        .withKeyType(KeyType.RANGE));


    userIndex.setKeySchema(userIndexKeySchema);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("Vote")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attributeDefinitions)
        .withProvisionedThroughput( new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withGlobalSecondaryIndexes(topicChoiceIndex, userIndex);

    dynamoDBClient.createTable(request);
  }
}
