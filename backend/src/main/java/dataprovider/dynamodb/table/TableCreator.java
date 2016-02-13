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
    if (tables.getTableNames().contains("User")) {
      dynamoDBClient.deleteTable("User");
      tables = dynamoDBClient.listTables();
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
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("userId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("category")
        .withAttributeType("S"));
    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.HASH));

    ArrayList<KeySchemaElement> userIndexKeySchema = new ArrayList<KeySchemaElement>();
    userIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("userId")
        .withKeyType(KeyType.HASH));

    GlobalSecondaryIndex userIndex = new GlobalSecondaryIndex()
        .withIndexName("userTopicIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
        .withKeySchema(userIndexKeySchema);

    ArrayList<KeySchemaElement> categoryKeySchema = new ArrayList<KeySchemaElement>();
    categoryKeySchema.add(new KeySchemaElement()
        .withAttributeName("category")
        .withKeyType(KeyType.HASH));

    GlobalSecondaryIndex categoryIndex = new GlobalSecondaryIndex()
        .withIndexName("categoryTopicIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
        .withKeySchema(categoryKeySchema);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("Topic")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attributeDefinitions)
        .withProvisionedThroughput( new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withGlobalSecondaryIndexes(userIndex, categoryIndex);

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
        .withAttributeName("userId")
        .withAttributeType("S"));
    attributeDefinitions.add(new AttributeDefinition()
        .withAttributeName("choiceId")
        .withAttributeType("S"));
    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement()
        .withAttributeName("voteId")
        .withKeyType(KeyType.HASH));

    GlobalSecondaryIndex topicVoteIndex = new GlobalSecondaryIndex()
        .withIndexName("topicVoteIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection()
            .withProjectionType(ProjectionType.KEYS_ONLY)
        );

    ArrayList<KeySchemaElement> topicVoteIndexKeySchema = new ArrayList<KeySchemaElement>();
    topicVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.HASH));
    topicVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("userId")
        .withKeyType(KeyType.RANGE));

    topicVoteIndex.setKeySchema(topicVoteIndexKeySchema);

    GlobalSecondaryIndex topicChoiceVoteIndex = new GlobalSecondaryIndex()
        .withIndexName("topicChoiceVoteIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection()
            .withProjectionType(ProjectionType.KEYS_ONLY)
        );

    ArrayList<KeySchemaElement> topicChoiceVoteIndexKeySchema = new ArrayList<KeySchemaElement>();
    topicChoiceVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.HASH));
    topicChoiceVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("choiceId")
        .withKeyType(KeyType.RANGE));

    topicChoiceVoteIndex.setKeySchema(topicChoiceVoteIndexKeySchema);

    GlobalSecondaryIndex userVoteIndex = new GlobalSecondaryIndex()
        .withIndexName("userVoteIndex")
        .withProvisionedThroughput(new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY));

    ArrayList<KeySchemaElement> userVoteIndexKeySchema = new ArrayList<KeySchemaElement>();
    userVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("userId")
        .withKeyType(KeyType.HASH));
    userVoteIndexKeySchema.add(new KeySchemaElement()
        .withAttributeName("topicId")
        .withKeyType(KeyType.RANGE));

    userVoteIndex.setKeySchema(userVoteIndexKeySchema);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName("Vote")
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attributeDefinitions)
        .withProvisionedThroughput( new ProvisionedThroughput()
            .withReadCapacityUnits((long) 1)
            .withWriteCapacityUnits((long) 1))
        .withGlobalSecondaryIndexes(topicVoteIndex, userVoteIndex, topicChoiceVoteIndex);

    dynamoDBClient.createTable(request);
  }
}
