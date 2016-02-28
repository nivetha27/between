package restservice.controller;

import core.Application;
import core.request.topic.Choice;
import core.request.topic.CreateTopicRequest;
import core.request.vote.VoteTopicRequest;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import restservice.Main;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static restservice.controller.TestUtils.json;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TopicsControllerTest {

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private MockMvc mockMvc;

  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  private final Application application = new Application(new DynamoDBDataProvider(new LocalDynamoDBClientProvider()));

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {
    mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
        hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

    Assert.assertNotNull("the JSON message converter must not be null",
        mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void createTopicsAndGetTopicsByCategory() throws Exception {
    String category = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    createTopics(category, userId, 25);
    this.mockMvc.perform(get("/topics/category")
        .param("category", category)
        .param("page", "1")
        .param("limit", "20")
        .contentType(contentType))
        .andExpect(status().isOk());
  }

  @Test
  public void createTopicsAndGetTopicsCreatedByUser() throws Exception {
    String category = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    createTopics(category, userId, 25);
    this.mockMvc.perform(get("/topics/user/created")
        .param("user_id", userId)
        .param("page", "1")
        .param("limit", "20")
        .contentType(contentType))
        .andExpect(status().isOk());
  }

  @Test
  public void voteTopicsAndGetTopicsVotedByUser() throws Exception {
    String category = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    voteTopics(userId, 25);
    this.mockMvc.perform(get("/topics/user/voted")
        .param("user_id", userId)
        .param("page", "1")
        .param("limit", "20")
        .contentType(contentType))
        .andExpect(status().isOk());
  }

  private void voteTopics(String userId, int count) throws Exception {
    for (int i = 0; i < count; i++) {
      String topicId = UUID.randomUUID().toString();
      createTopic(topicId);
      VoteTopicRequest voteTopicRequest = new VoteTopicRequest();
      voteTopicRequest.setUserId(userId);
      voteTopicRequest.setTopicId(topicId);
      voteTopicRequest.setChoiceId("1");
      voteTopicRequest.setComment("comment1");
      String voteJson = json(voteTopicRequest, mappingJackson2HttpMessageConverter);
      this.mockMvc.perform(post("/vote")
          .contentType(contentType)
          .content(voteJson))
          .andExpect(status().isOk());
    }
  }

  private void createTopic(String topicId) throws Exception {
    core.entities.Choice choice1 = new core.entities.Choice();
    choice1.setChoiceId(1);
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    choice1.setVotes(0);
    core.entities.Choice choice2 = new core.entities.Choice();
    choice2.setChoiceId(1);
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    choice2.setVotes(0);
    core.entities.Topic topic = new core.entities.Topic();
    topic.setTopicId(topicId);
    topic.setCategory(UUID.randomUUID().toString());
    topic.setDateTime(DateTime.now());
    topic.setDescription("description");
    topic.setUserId(UUID.randomUUID().toString());
    topic.setChoices(Arrays.asList(choice1, choice2));
    application.createTopic(topic);
  }

  private void createTopics(String category, String userId, int count) throws Exception {
    Choice choice1 = new Choice();
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    Choice choice2 = new Choice();
    choice2.setCaption("caption2");
    choice2.setImageUrl("url2");
    for (int i = 0; i < count; i++) {
      CreateTopicRequest createTopicRequest = new CreateTopicRequest();
      createTopicRequest.setCategory(category);
      createTopicRequest.setUserId(userId);
      createTopicRequest.setDescription("description");
      createTopicRequest.setChoices(Arrays.asList(choice1, choice2));
      String topicJson = json(createTopicRequest, mappingJackson2HttpMessageConverter);
      this.mockMvc.perform(post("/topic")
          .contentType(contentType)
          .content(topicJson))
          .andExpect(status().isCreated());
    }
  }
}
