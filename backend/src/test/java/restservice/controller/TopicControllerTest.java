package restservice.controller;

import core.Application;
import core.entities.Topic;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import restservice.Main;
import core.request.topic.Choice;
import core.request.topic.CreateTopicRequest;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static restservice.controller.TestUtils.json;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TopicControllerTest {

  private final Application application = new Application(new DynamoDBDataProvider(new LocalDynamoDBClientProvider()));

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private MockMvc mockMvc;

  private HttpMessageConverter mappingJackson2HttpMessageConverter;

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
  public void createTopic() throws Exception {
    CreateTopicRequest createTopicRequest = new CreateTopicRequest();
    createTopicRequest.setDescription("description");
    createTopicRequest.setCategory("category");
    Choice choice1 = new Choice();
    choice1.setCaption("caption1");
    choice1.setImageUrl("url1");
    Choice choice2 = new Choice();
    choice2.setCaption("caption1");
    choice2.setImageUrl("url1");
    createTopicRequest.setChoices(Arrays.asList(choice1, choice2));
    createTopicRequest.setUserId(UUID.randomUUID().toString());
    String topicJson = json(createTopicRequest, mappingJackson2HttpMessageConverter);
    this.mockMvc.perform(post("/user")
        .contentType(contentType)
        .content(topicJson))
        .andExpect(status().isCreated());
  }

  @Test
  public void testDeleteTopic() throws Exception {
    String topicId = UUID.randomUUID().toString();
    DateTime now = DateTime.now();
    Topic topic = new Topic();
    topic.setTopicId(topicId);
    topic.setCategory("category");
    topic.setDateTime(now);
    topic.setDescription("description");
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
    topic.setChoices(Arrays.asList(choice1, choice2));
    String userId = UUID.randomUUID().toString();
    topic.setUserId(userId);
    application.createTopic(topic);

    this.mockMvc.perform(delete("/topic")
        .param("id", topicId))
        .andExpect(status().isAccepted());
  }
}
