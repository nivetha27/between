package dataprovider.dynamodb.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class LocalDynamoDBClientProvider implements DynamoDBClientProvider {

  final private AmazonDynamoDBClient client = getDynamoDBClient();

  private static AmazonDynamoDBClient getDynamoDBClient() {
    AWSCredentials awsCredentials = new BasicAWSCredentials("xxxx", "yyyy");
    AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredentials);
    amazonDynamoDBClient.setEndpoint("http://localhost:8000");
    return amazonDynamoDBClient;
  }

  @Override
  public AmazonDynamoDBClient getClient() {
    return client;
  }
}
