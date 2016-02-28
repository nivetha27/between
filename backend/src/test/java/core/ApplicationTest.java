package core;

import core.entities.Choice;
import core.entities.User;
import core.entities.Topic;
import core.exceptions.EntityNotFoundException;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import dataprovider.dynamodb.model.Vote;
import dataprovider.dynamodb.table.TableCreator;
import dataprovider.dynamodb.table.TableCreatorFactory;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

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
  public void testUpdateUser() {
    List<String> categories = Arrays.asList("category1", "category2");
    String userId = UUID.randomUUID().toString();
    User user = new User();
    user.setUserId(userId);
    user.setCategories(categories);
    application.createUser(user);
    user = application.getUser(userId);
    Assert.assertEquals(userId, user.getUserId());
    Assert.assertEquals(categories, user.getCategories());

    List<String> updatedCategories = Arrays.asList("category3", "category4");
    user.setCategories(updatedCategories);
    application.updateUser(user);

    user = application.getUser(userId);
    Assert.assertEquals(userId, user.getUserId());
    Assert.assertEquals(updatedCategories, user.getCategories());
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteUser() {
    List<String> categories = Arrays.asList("category1", "category2");
    String userId = UUID.randomUUID().toString();
    User user = new User();
    user.setUserId(userId);
    user.setCategories(categories);
    application.createUser(user);
    user = application.getUser(userId);
    Assert.assertEquals(userId, user.getUserId());
    Assert.assertEquals(categories, user.getCategories());

    application.deleteUser(userId);
    application.getUser(userId);
  }

  @Test
  public void testCreateAndGetTopic() {
    String topicId = UUID.randomUUID().toString();
    DateTime now = DateTime.now();
    Topic topic = new Topic();
    topic.setTopicId(topicId);
    topic.setCategory("category");
    topic.setDateTime(now);
    topic.setDescription("description");
    Choice choice1 = new Choice();
    choice1.setChoiceId(1);
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    choice1.setVotes(0);
    Choice choice2 = new Choice();
    choice2.setChoiceId(1);
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    choice2.setVotes(0);
    topic.setChoices(Arrays.asList(choice1, choice2));
    String userId = UUID.randomUUID().toString();
    topic.setUserId(userId);
    application.createTopic(topic);

    topic = application.getTopic(topicId);
    Assert.assertEquals(topic.getTopicId(), topicId);
    Assert.assertEquals(topic.getUserId(), userId);
    Assert.assertEquals(topic.getCategory(), "category");
    Assert.assertEquals(topic.getDescription(), "description");
    Assert.assertEquals(topic.getDateTime().toString(), now.toString());
    Assert.assertEquals(topic.getChoices().size(), 2);
    Assert.assertEquals(topic.getChoices().get(0).getCaption(), "caption1");
    Assert.assertEquals(topic.getChoices().get(1).getCaption(), "caption2");
    Assert.assertEquals(topic.getChoices().get(0).getImageUrl(), "url1");
    Assert.assertEquals(topic.getChoices().get(1).getImageUrl(), "url2");
  }

  @Test
  public void voteTopic() {
    String topicId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    createTopic(topicId);
    core.entities.Vote vote = new core.entities.Vote();
    vote.setTopicId(topicId);
    vote.setUserId(userId);
    vote.setComment("comment");
    vote.setChoiceId("1");
    vote.setDateTime(DateTime.now());
    vote.setVoteId(UUID.randomUUID().toString());
    application.voteTopic(vote);

    List<Topic> topics = application.listTopicsVotedByUser(userId, 1, 1);
    Assert.assertEquals(1, topics.size());
    Assert.assertEquals(topics.get(0).getTopicId(), topicId);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteTopic() {
    String topicId = UUID.randomUUID().toString();
    DateTime now = DateTime.now();
    Topic topic = new Topic();
    topic.setTopicId(topicId);
    topic.setCategory("category");
    topic.setDateTime(now);
    topic.setDescription("description");
    Choice choice1 = new Choice();
    choice1.setChoiceId(1);
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    choice1.setVotes(0);
    Choice choice2 = new Choice();
    choice2.setChoiceId(1);
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    choice2.setVotes(0);
    topic.setChoices(Arrays.asList(choice1, choice2));
    String userId = UUID.randomUUID().toString();
    topic.setUserId(userId);
    application.createTopic(topic);

    topic = application.getTopic(topicId);
    Assert.assertEquals(topic.getTopicId(), topicId);

    application.deleteTopic(topicId);
    application.getTopic(topicId);

  }

  @Test
  public void testListTopicsByCategory() {
    String category = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    createTopics(category, userId, 20);
    List<Topic> topics = application.listTopicsByCategory(category, 1, 10);
    Assert.assertEquals(10, topics.size());
    topics = application.listTopicsByCategory(category, 2, 15);
    Assert.assertEquals(5, topics.size());
    topics = application.listTopicsByCategory(category, 1, 40);
    Assert.assertEquals(20, topics.size());
  }

  @Test
  public void testListTopicsCreatedByUser() {
    String category = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    createTopics(category, userId, 20);
    List<Topic> topics = application.listTopicsCreatedByUser(userId, 1, 10);
    Assert.assertEquals(10, topics.size());
    topics = application.listTopicsCreatedByUser(userId, 2, 15);
    Assert.assertEquals(5, topics.size());
    topics = application.listTopicsCreatedByUser(userId, 1, 40);
    Assert.assertEquals(20, topics.size());
  }

  @Test
  public void testListTopicsVotedByUser() {
    String userId = UUID.randomUUID().toString();
    voteTopics(userId, 20);
    List<Topic> topics = application.listTopicsVotedByUser(userId, 1, 10);
    Assert.assertEquals(10, topics.size());
    topics = application.listTopicsVotedByUser(userId, 2, 15);
    Assert.assertEquals(5, topics.size());
    topics = application.listTopicsVotedByUser(userId, 1, 40);
    Assert.assertEquals(20, topics.size());
  }


  private void voteTopics(String userID, int num) {
    for (int i = 0; i < num; i++) {
      String topicId = UUID.randomUUID().toString();
      createTopic(topicId);
      core.entities.Vote vote = new core.entities.Vote();
      vote.setTopicId(topicId);
      vote.setUserId(userID);
      vote.setComment("comment");
      vote.setChoiceId("1");
      vote.setDateTime(DateTime.now());
      vote.setVoteId(UUID.randomUUID().toString());
      application.voteTopic(vote);
    }
  }

  private void createTopic(String topicId) {
    Choice choice1 = new Choice();
    choice1.setChoiceId(1);
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    choice1.setVotes(0);
    Choice choice2 = new Choice();
    choice2.setChoiceId(1);
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    choice2.setVotes(0);
    Topic topic = new Topic();
    topic.setTopicId(topicId);
    topic.setCategory(UUID.randomUUID().toString());
    topic.setDateTime(DateTime.now());
    topic.setDescription("description");
    topic.setUserId(UUID.randomUUID().toString());
    topic.setChoices(Arrays.asList(choice1, choice2));
    application.createTopic(topic);
  }

  private void createTopics(String category, String userId, int num) {
    Choice choice1 = new Choice();
    choice1.setChoiceId(1);
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    choice1.setVotes(0);
    Choice choice2 = new Choice();
    choice2.setChoiceId(1);
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    choice2.setVotes(0);
    for (int i = 0; i < num; i++) {
      Topic topic = new Topic();
      topic.setTopicId(UUID.randomUUID().toString());
      topic.setCategory(category);
      topic.setDateTime(DateTime.now());
      topic.setDescription("description");
      topic.setUserId(userId);
      topic.setChoices(Arrays.asList(choice1, choice2));
      application.createTopic(topic);
    }
  }
}
