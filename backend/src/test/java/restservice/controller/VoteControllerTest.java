package restservice.controller;

import core.request.vote.VoteTopicRequest;
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

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static restservice.controller.TestUtils.json;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootConfiguration.class)
@WebAppConfiguration
public class VoteControllerTest {

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
  public void testCreateVote() throws Exception {
    VoteTopicRequest voteTopicRequest = new VoteTopicRequest();
    voteTopicRequest.setUserId(UUID.randomUUID().toString());
    voteTopicRequest.setTopicId(UUID.randomUUID().toString());
    voteTopicRequest.setChoiceId("1");
    voteTopicRequest.setComment("comment1");
    String voteJson = json(voteTopicRequest, mappingJackson2HttpMessageConverter);
    this.mockMvc.perform(post("/vote")
        .contentType(contentType)
        .content(voteJson))
        .andExpect(status().isOk());
  }

  @Test
  public void testAlreadyVotedException() throws Exception {
    String userId = UUID.randomUUID().toString();
    String topicId = UUID.randomUUID().toString();
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

    this.mockMvc.perform(post("/vote")
        .contentType(contentType)
        .content(voteJson))
        .andExpect(status().isAlreadyReported());
  }
}
