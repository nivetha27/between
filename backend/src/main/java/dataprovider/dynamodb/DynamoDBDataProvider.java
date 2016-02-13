package dataprovider.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.ObjectSetToStringSetMarshaller;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.base.Strings;
import core.entities.Choice;
import core.entities.Vote;
import core.entities.Topic;
import core.entities.User;
import core.exceptions.AlreadyVotedException;
import core.exceptions.EntityNotFoundException;
import core.response.TopicList;
import dataprovider.DataProvider;
import dataprovider.dynamodb.client.DynamoDBClientProvider;

import java.util.*;
import java.util.stream.Collectors;

public final class DynamoDBDataProvider implements DataProvider {

  private final AmazonDynamoDBClient dynamoDBClient;
  private final DynamoDBMapper mapper;

  public DynamoDBDataProvider(DynamoDBClientProvider dynamoDBClientProvider) {
    this.dynamoDBClient = dynamoDBClientProvider.getClient();
    this.mapper = new DynamoDBMapper(this.dynamoDBClient);
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
  public void updateUser(User user) {
    dataprovider.dynamodb.model.User userModel = mapper.load(dataprovider.dynamodb.model.User.class, user.getUserId());
    if (userModel == null) {
      throw new EntityNotFoundException("user does not exists. userId: " + user.getUserId());
    }

    if (user.getCategories() != null) {
      userModel.setCategories(user.getCategories());
    }
    mapper.save(userModel);
  }

  @Override
  public void deleteUser(String userId) {
    dataprovider.dynamodb.model.User userModel = mapper.load(dataprovider.dynamodb.model.User.class, userId);
    if (userModel == null) {
      throw new EntityNotFoundException("User does not exists. UserId: " + userId);
    }
    mapper.delete(userModel);
  }

  @Override
  public void createTopic(Topic topic) {
    dataprovider.dynamodb.model.Topic topicModel = dataprovider.dynamodb.model.Topic.fromTopic(topic);
    mapper.save(topicModel);
  }

  @Override
  public void deleteTopic(String topicId) {
    dataprovider.dynamodb.model.Topic topicModel = mapper.load(dataprovider.dynamodb.model.Topic.class, topicId);
    if (topicModel == null) {
      throw new EntityNotFoundException("Topic does not exists. TopicId: " + topicId);
    }
    mapper.delete(topicModel);
  }

  @Override
  public void voteTopic(Vote vote) {

    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(vote.getUserId()));
    attributeValueMap.put(":val2", new AttributeValue().withS(vote.getTopicId()));

    DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote>()
            .withIndexName("userVoteIndex")
            .withKeyConditionExpression("userId = :val1 and topicId = :val2")
            .withExpressionAttributeValues(attributeValueMap)
            .withConsistentRead(false);

    QueryResultPage resultPage =
        mapper.queryPage(dataprovider.dynamodb.model.Vote.class, queryExpression);

    if (resultPage.getCount() > 0) {
      throw new AlreadyVotedException(vote.getTopicId(), vote.getUserId());
    }

    dataprovider.dynamodb.model.Vote voteModel = dataprovider.dynamodb.model.Vote.fromVote(vote);
    mapper.save(voteModel);
  }

  @Override
  public void incrementVote(String topicId, Integer choiceId, long numVotes) {
    dataprovider.dynamodb.model.Topic topicModel;
    topicModel = mapper.load(dataprovider.dynamodb.model.Topic.class, topicId);
    List<Choice> choices = topicModel.getChoices();
    if (choices != null) {
      for (Choice choice : choices) {
        if (choice.getChoiceId().equals(choiceId)) {
          choice.setVotes(choice.getVotes() + numVotes);
          mapper.save(topicModel);
          break;
        }
      }
    }
  }

  @Override
  public Optional<Topic> getTopic(String topicId) {
    Optional<Topic> topic = Optional.empty();
    dataprovider.dynamodb.model.Topic topicModel;
    topicModel = mapper.load(dataprovider.dynamodb.model.Topic.class, topicId);
    if (topicModel != null) {
      topic = Optional.of(topicModel.toTopic());
    }
    return topic;
  }

  @Override
  public List<Topic> getTopicsByCategory(String category, Integer pageNumber, Integer limit) {

    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(category));

    DynamoDBQueryExpression<dataprovider.dynamodb.model.Topic> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Topic>()
            .withIndexName("categoryTopicIndex")
            .withKeyConditionExpression("category = :val1")
            .withExpressionAttributeValues(attributeValueMap)
            .withLimit(limit)
            .withConsistentRead(false);

    QueryResultPage resultPage = null;
    for (int i = 1; i <= pageNumber; i++) {
      if (resultPage != null) {
        if (resultPage.getLastEvaluatedKey() != null) {
          queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
        } else {
          // breaking because there is no more results to paginate on when exclusive key is null.
          break;
        }
      }
      resultPage = mapper.queryPage(dataprovider.dynamodb.model.Topic.class, queryExpression);
    }

    List<Topic> topics = new ArrayList<>();
    if (resultPage.getResults() != null && !resultPage.getResults().isEmpty()) {
        List<Object> pageResults = resultPage.getResults();
        Map<String, List<Object>> objectMap = mapper.batchLoad(pageResults);
        List<Object> topicObjects = objectMap.get("Topic");
        if (topicObjects != null) {
          for (Object object : topicObjects) {
            topics.add(((dataprovider.dynamodb.model.Topic) object).toTopic());
          }
        }
    }
    return topics;
  }

  @Override
  public List<Topic> getTopicsCreatedByUser(String userId, Integer pageNumber, Integer limit) {

    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(userId));

    DynamoDBQueryExpression<dataprovider.dynamodb.model.Topic> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Topic>()
            .withIndexName("userTopicIndex")
            .withKeyConditionExpression("userId = :val1")
            .withExpressionAttributeValues(attributeValueMap)
            .withLimit(limit)
            .withConsistentRead(false);

    QueryResultPage resultPage = null;
    for (int i = 1; i <= pageNumber; i++) {
      if (resultPage != null) {
        if (resultPage.getLastEvaluatedKey() != null) {
          queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
        } else {
          // breaking because there is no more results to paginate on when exclusive key is null.
          break;
        }
      }
      resultPage = mapper.queryPage(dataprovider.dynamodb.model.Topic.class, queryExpression);
    }

    List<Topic> topics = new ArrayList<>();
    if (resultPage.getResults() != null && !resultPage.getResults().isEmpty()) {
        List<Object> pageResults = resultPage.getResults();
        Map<String, List<Object>> objectMap = mapper.batchLoad(pageResults);
        List<Object> topicObjects = objectMap.get("Topic");
        if (topicObjects != null) {
          for (Object object : topicObjects) {
            topics.add(((dataprovider.dynamodb.model.Topic) object).toTopic());
          }
        }
    }

    return topics;
  }

  @Override
  public List<Topic> getTopicsVotedByUser(String userId, Integer pageNumber, Integer limit) {
    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(userId));

    DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote>()
            .withIndexName("userVoteIndex")
            .withKeyConditionExpression("userId = :val1")
            .withExpressionAttributeValues(attributeValueMap)
            .withLimit(limit)
            .withConsistentRead(false);

    QueryResultPage resultPage = null;
    for (int i = 1; i <= pageNumber; i++) {
      if (resultPage != null) {
        if (resultPage.getLastEvaluatedKey() != null) {
          queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
        } else {
          // breaking because there is no more results to paginate on when exclusive key is null.
          break;
        }
      }
      resultPage = mapper.queryPage(dataprovider.dynamodb.model.Vote.class, queryExpression);
    }

    List<Topic> topics = new ArrayList<>();
    if (resultPage.getResults() != null && !resultPage.getResults().isEmpty()) {
        List<Object> pageResults = resultPage.getResults();
        List<Object> topicIdModels = new ArrayList<>();
        for (Object object : pageResults) {
          dataprovider.dynamodb.model.Vote voteIdModel = (dataprovider.dynamodb.model.Vote) object;
          dataprovider.dynamodb.model.Topic topicIdModel = new dataprovider.dynamodb.model.Topic();
          topicIdModel.setTopicId(voteIdModel.getTopicId());
          topicIdModels.add(topicIdModel);
        }
        Map<String, List<Object>> objectMap = mapper.batchLoad(topicIdModels);
        List<Object> topicObjects = objectMap.get("Topic");
        if (topicObjects != null) {
          for (Object object : topicObjects) {
            topics.add(((dataprovider.dynamodb.model.Topic) object).toTopic());
          }
        }
    }
    return topics;
  }
}
