package aggregator;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dataprovider.DataProvider;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.client.DynamoDBClientProvider;
import dataprovider.dynamodb.model.Vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteAggregator {

  private final Integer NUM_RESULTS_LIMIT = 1000;

  private final DataProvider dataProvider;
  private final AmazonDynamoDBClient dynamoDBClient;
  private final DynamoDBMapper mapper;

  public VoteAggregator(DynamoDBClientProvider dynamoDBClientProvider) {
    this.dataProvider = new DynamoDBDataProvider(dynamoDBClientProvider);
    this.dynamoDBClient = dynamoDBClientProvider.getClient();
    this.mapper = new DynamoDBMapper(this.dynamoDBClient);
  }

  public void removeDuplicateVotes(String topicId) {
    System.out.println("Removing duplicates: " + topicId);
    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(topicId));

    DynamoDBQueryExpression<Vote> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote>()
            .withIndexName("topicVoteIndex")
            .withKeyConditionExpression("topicId = :val1")
            .withExpressionAttributeValues(attributeValueMap)
            .withLimit(NUM_RESULTS_LIMIT)
            .withConsistentRead(false);

    QueryResultPage resultPage = null;
    String lastSeenUserId = null;
    while (resultPage == null || resultPage.getLastEvaluatedKey() != null) {
      if (resultPage != null && resultPage.getLastEvaluatedKey() != null) {
        queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
      }
      resultPage = mapper.queryPage(dataprovider.dynamodb.model.Vote.class, queryExpression);

      List<Object> results = resultPage.getResults();
      if (results != null && !results.isEmpty()) {
        for (Object result : results) {
          Vote vote = (Vote) result;
          if (lastSeenUserId != null) {
            if (vote.getUserId().equals(lastSeenUserId)) {
              mapper.delete(vote);
            } else {
              lastSeenUserId = vote.getUserId();
            }
          }
        }
      }
    }
  }

  public void aggregateVotes(String topicId) {
    aggregateVotes(topicId, 1);
    aggregateVotes(topicId, 2);
  }

  private void aggregateVotes(String topicId, Integer choiceId) {
    System.out.println("aggregating votes: " + topicId + " choice: " + choiceId);
    Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
    attributeValueMap.put(":val1", new AttributeValue().withS(topicId));
    attributeValueMap.put(":val2", new AttributeValue().withS(String.valueOf(choiceId)));

    DynamoDBQueryExpression<Vote> queryExpression =
        new DynamoDBQueryExpression<dataprovider.dynamodb.model.Vote>()
            .withIndexName("topicChoiceVoteIndex")
            .withKeyConditionExpression("topicId = :val1 and choiceId = :val2")
            .withLimit(NUM_RESULTS_LIMIT)
            .withConsistentRead(false)
            .withExpressionAttributeValues(attributeValueMap);

    QueryResultPage resultPage = null;
    long choiceVotes = 0;
    while (resultPage == null || resultPage.getLastEvaluatedKey() != null) {
      if (resultPage != null && resultPage.getLastEvaluatedKey() != null) {
        queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
      }
      resultPage = mapper.queryPage(dataprovider.dynamodb.model.Vote.class, queryExpression);

      List<Object> results = resultPage.getResults();
      if (results != null) {
        System.out.println("vote size: " + results.size());
        choiceVotes += results.size();
      }
    }
    dataProvider.incrementVote(topicId, choiceId, choiceVotes);
  }
}
