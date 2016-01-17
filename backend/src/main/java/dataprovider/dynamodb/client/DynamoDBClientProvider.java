package dataprovider.dynamodb.client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public interface DynamoDBClientProvider {

  AmazonDynamoDBClient getClient();
}
