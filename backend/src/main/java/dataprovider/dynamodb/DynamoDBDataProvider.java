package dataprovider.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import core.entities.User;
import dataprovider.DataProvider;

public final class DynamoDBDataProvider implements DataProvider {

  private final AmazonDynamoDBClient dynamoDBClient;
  private final DynamoDBMapper mapper;

  public DynamoDBDataProvider(AmazonDynamoDBClient dynamoDBClient) {
    this.dynamoDBClient = dynamoDBClient;
    this.mapper = new DynamoDBMapper(this.dynamoDBClient);
  }

  @Override
  public void createUser(User user) {
    dataprovider.dynamodb.model.User userModel = dataprovider.dynamodb.model.User.fromUser(user);
    mapper.save(userModel);
  }
}
