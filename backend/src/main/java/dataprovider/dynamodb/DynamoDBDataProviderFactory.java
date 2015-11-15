package dataprovider.dynamodb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDBDataProviderFactory {

  public static DynamoDBDataProvider create() {
    AmazonDynamoDBClient amazonDynamoDBClient = getDynamoDBClient();
    DynamoDBDataProvider dynamoDBDataProvider = new DynamoDBDataProvider(amazonDynamoDBClient);
    return dynamoDBDataProvider;
  }

  private static AmazonDynamoDBClient getDynamoDBClient() {
    AWSCredentials awsCredentials = new BasicAWSCredentials("xxxx", "yyyy");
    AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredentials);
    amazonDynamoDBClient.setEndpoint("http://localhost:8000");
    return amazonDynamoDBClient;
  }
}
