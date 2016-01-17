package dataprovider.dynamodb.table;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class TableCreatorFactory {

  public static TableCreator create() {
    AmazonDynamoDBClient amazonDynamoDBClient = getDynamoDBClient();
    TableCreator tableCreator = new TableCreator(amazonDynamoDBClient);
    return tableCreator;
  }

  private static AmazonDynamoDBClient getDynamoDBClient() {
    AWSCredentials awsCredentials = new BasicAWSCredentials("xxxx", "yyyy");
    AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredentials);
    amazonDynamoDBClient.setEndpoint("http://localhost:8000");
    return amazonDynamoDBClient;
  }
}
