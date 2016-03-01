package dataprovider.dynamodb.client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class InstanceProfileDynamoDBClientProvider implements DynamoDBClientProvider {

  @Override
  public AmazonDynamoDBClient getClient() {
    return new AmazonDynamoDBClient();
  }
}
