package restservice.controller;

import com.google.common.collect.Lists;
import core.entities.User;
import core.request.user.CreateUserRequest;
import core.request.user.UpdateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import restservice.Main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static restservice.controller.TestUtils.json;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class UserControllerTest {
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
  public void testCreateAndGetUser() throws Exception {
    String userId = UUID.randomUUID().toString();
    List<String> categories = Arrays.asList("category1", "category2");
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUserId(userId);
    createUserRequest.setCategories(categories);
    String userJson = json(createUserRequest, mappingJackson2HttpMessageConverter);
    this.mockMvc.perform(post("/user")
        .contentType(contentType)
        .content(userJson))
        .andExpect(status().isCreated());
    this.mockMvc.perform(get("/user")
    .param("id", userId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.userId", is(userId)))
        .andExpect(jsonPath("$.categories", is(categories)));
  }

  @Test
  public void testUpdateUser() throws Exception {
    String userId = UUID.randomUUID().toString();
    List<String> categories = Arrays.asList("category1", "category2");
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUserId(userId);
    createUserRequest.setCategories(categories);
    String userJson = json(createUserRequest, mappingJackson2HttpMessageConverter);
    this.mockMvc.perform(post("/user")
        .contentType(contentType)
        .content(userJson))
        .andExpect(status().isCreated());
    this.mockMvc.perform(get("/user")
        .param("id", userId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.userId", is(userId)))
        .andExpect(jsonPath("$.categories", is(categories)));

    List<String> updatedCategories = Arrays.asList("category3", "category4");
    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setUserId(userId);
    updateUserRequest.setCategories(updatedCategories);
    String updatedUserJson = json(updateUserRequest, mappingJackson2HttpMessageConverter);

    this.mockMvc.perform(put("/user")
        .contentType(contentType)
        .content(updatedUserJson))
        .andExpect(status().isOk());
    this.mockMvc.perform(get("/user")
        .param("id", userId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.userId", is(userId)))
        .andExpect(jsonPath("$.categories", is(updatedCategories)));
  }

  @Test
  public void testDeleteUser() throws Exception {
    String userId = UUID.randomUUID().toString();
    List<String> categories = Arrays.asList("category1", "category2");
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUserId(userId);
    createUserRequest.setCategories(categories);
    String userJson = json(createUserRequest, mappingJackson2HttpMessageConverter);
    this.mockMvc.perform(post("/user")
        .contentType(contentType)
        .content(userJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(delete("/user")
        .param("id", userId))
        .andExpect(status().isOk());
  }
}
