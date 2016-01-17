package dataprovider.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import core.entities.Choice;
import core.entities.Vote;
import core.entities.Topic;
import core.entities.User;
import dataprovider.DataProvider;
import dataprovider.dynamodb.client.DynamoDBClientProvider;

import java.util.Optional;

public final class DynamoDBDataProvider implements DataProvider {

  private final AmazonDynamoDBClient dynamoDBClient;
  private final DynamoDBMapper mapper;
  private final DynamoDB dynamoDB;

  public DynamoDBDataProvider(DynamoDBClientProvider dynamoDBClientProvider) {
    this.dynamoDBClient = dynamoDBClientProvider.getClient();
    this.mapper = new DynamoDBMapper(this.dynamoDBClient);
    this.dynamoDB = new DynamoDB(this.dynamoDBClient);
  }

  @Override
  public void createUser(User user) {
    dataprovider.dynamodb.model.User userModel = dataprovider.dynamodb.model.User.fromUser(user);
    mapper.save(userModel);
  }

  @Override
  public Optional<User> getUser(String userId) {
    Optional<User> user = Optional.empty();
    dataprovider.dynamodb.model.User userModel = mapper.load(dataprovider.dynamodb.model.User.class, userId);
    if (userModel != null) {
      user = Optional.of(userModel.toUser());
    }
    return user;
  }

  @Override
  public void createTopic(Topic topic) {
    dataprovider.dynamodb.model.Topic topicModel = dataprovider.dynamodb.model.Topic.fromTopic(topic);
    mapper.save(topicModel);
  }

  @Override
  public void voteTopic(Vote vote) {
    dataprovider.dynamodb.model.Vote voteModel = dataprovider.dynamodb.model.Vote.fromVote(vote);
    mapper.save(voteModel);
  }

  @Override
  public void incrementVote(String topicId, Integer choiceId, long numVotes) {
    dataprovider.dynamodb.model.Topic topicModel;
    topicModel = mapper.load(dataprovider.dynamodb.model.Topic.class, topicId);
    Choice choice = topicModel.getChoices().get(choiceId);
    choice.setVotes(choice.getVotes() + numVotes);
    mapper.save(topicModel);
  }

  @Override
  public Topic getTopic(String topicId) {
    dataprovider.dynamodb.model.Topic topicModel;
    topicModel = mapper.load(dataprovider.dynamodb.model.Topic.class, topicId);
    System.out.println(topicModel.getChoices());
    return topicModel.toTopic();
  }
}
