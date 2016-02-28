package core;

import core.entities.Topic;
import core.entities.User;
import core.entities.Vote;
import core.exceptions.EntityNotFoundException;
import dataprovider.DataProvider;
import imageuploader.ImageUploader;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class Application {

  private final DataProvider dataProvider;

  private final ImageUploader imageUploader;

  public Application(DataProvider dataProvider, ImageUploader imageUploader)
  {
    this.dataProvider = dataProvider;
    this.imageUploader = imageUploader;
  }

  public void createUser(User user) {
    dataProvider.createUser(user);
  }

  public User getUser(String userId) {
    Optional<User> user = dataProvider.getUser(userId);
    if (user.isPresent()) {
      return user.get();
    }
    throw new EntityNotFoundException("user not found");
  }

  public void updateUser(User user) {
    dataProvider.updateUser(user);
  }

  public void deleteUser(String userId) {
    dataProvider.deleteUser(userId);
  }

  public String createTopic(Topic topic) {
      return dataProvider.createTopic(topic);
  }

  public Topic getTopic(String topicId) {
    Optional<Topic> topic = dataProvider.getTopic(topicId);
    if (topic.isPresent()) {
      return topic.get();
    }
    throw new EntityNotFoundException("topic not found");
  }

  public void voteTopic(Vote vote) {
      dataProvider.voteTopic(vote);
  }

  public void deleteTopic(String topicId) {
    dataProvider.deleteTopic(topicId);
  }

  public List<Topic> listTopicsByCategory(String category, Integer pageNumber, Integer limit) {
    return dataProvider.getTopicsByCategory(category, pageNumber, limit);
  }

  public List<Topic> listTopicsCreatedByUser(String userId, Integer pageNumber, Integer limit) {
    return dataProvider.getTopicsCreatedByUser(userId, pageNumber, limit);
  }

  public List<Topic> listTopicsVotedByUser(String userId, Integer pageNumber, Integer limit) {
    return dataProvider.getTopicsVotedByUser(userId, pageNumber, limit);
  }

  public String uploadImage(InputStream inputStream) {
    return imageUploader.uploadImage(inputStream);
  }
}
