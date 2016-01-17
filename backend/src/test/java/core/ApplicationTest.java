package core;

import core.entities.User;
import core.entities.Vote;
import core.entities.Topic;
import core.requests.*;
import core.responses.*;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.DynamoDBDataProviderFactory;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import dataprovider.dynamodb.table.TableCreator;
import dataprovider.dynamodb.table.TableCreatorFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ApplicationTest {

  private final static TableCreator tableCreator = TableCreatorFactory.create();

  private final Application application = new Application(new DynamoDBDataProvider(new LocalDynamoDBClientProvider()));

  @BeforeClass
  public static void init() {
    tableCreator.recreateTables();
  }

  @Test
  public void testCreateAndGetUser() {
    List<String> categories = Arrays.asList("category1", "category2");
    String userId = UUID.randomUUID().toString();
    User user = new User();
    user.setUserId(userId);
    user.setCategories(categories);
    application.createUser(user);
    user = application.getUser(userId);
    Assert.assertEquals(userId, user.getUserId());
    Assert.assertEquals(categories, user.getCategories());
  }

  @Test
  public void testCreateTopic() {
    CreateTopicRequest.Choice choice1 = new CreateTopicRequest.Choice("caption1", "url1");
    CreateTopicRequest.Choice choice2 = new CreateTopicRequest.Choice("caption2", "url2");
    CreateTopicRequest createTopicRequest = new CreateTopicRequest.Builder("description1", Arrays.asList(choice1, choice2))
        .withCategory("category1")
        .create();
    CreateTopicResponse createTopicResponse = application.createTopic(createTopicRequest);
    Assert.assertEquals(createTopicResponse.isSuccess(), true);
  }

  @Test
  public void testVoteTopic() {
    String topicId = UUID.randomUUID().toString();
    String choiceId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    VoteTopicRequest request = new VoteTopicRequest.Builder(topicId, choiceId, userId)
        .withComment("comment1")
        .create();
    VoteTopicResponse response = application.voteChoice(request);
    Assert.assertEquals(response.isSuccess(), true);
  }

  @Test
  public void testGetTopic() {
    CreateTopicRequest.Choice choice1 = new CreateTopicRequest.Choice("caption1", "url1");
    CreateTopicRequest.Choice choice2 = new CreateTopicRequest.Choice("caption2", "url2");
    CreateTopicRequest createTopicRequest = new CreateTopicRequest.Builder("description1", Arrays.asList(choice1, choice2))
        .withCategory("category2")
        .create();
    CreateTopicResponse createTopicResponse = application.createTopic(createTopicRequest);
    GetTopicRequest getTopicRequest = new GetTopicRequest.Builder(createTopicRequest.getTopicId())
        .create();
    GetTopicResponse getTopicResponse = application.getTopic(getTopicRequest);
    Topic topic = getTopicResponse.getTopic();
    Assert.assertEquals(topic.getTopicId(), createTopicRequest.getTopicId());
    Assert.assertEquals(topic.getCategory(), "category2");
    System.out.println(topic.getChoices().get(0));
    Assert.assertEquals(topic.getChoices().get(0).getCaption(), "caption1");
    Assert.assertEquals(topic.getChoices().get(1).getCaption(), "caption2");
    Assert.assertEquals(topic.getChoices().get(0).getImageUrl(), "url1");
    Assert.assertEquals(topic.getChoices().get(1).getImageUrl(), "url2");
  }
}
