package dataprovider;

import core.entities.Topic;
import core.entities.User;
import core.entities.Vote;
import core.response.TopicList;

import java.util.List;
import java.util.Optional;

public interface DataProvider {

  void createUser(User user);

  Optional<User> getUser(String userId);

  void updateUser(User user);

  void deleteUser(String userId);

  String createTopic(Topic topic);

  void deleteTopic(String topicId);

  void voteTopic(Vote vote);

  void incrementVote(String topicId, Integer choiceId, long numVotes);

  Optional<Topic> getTopic(String topicId);

  List<Topic> getTopicsByCategory(String category, Integer pageNumber, Integer limit);

  List<Topic> getTopicsCreatedByUser(String userId, Integer pageNumber, Integer limit);

  List<Topic> getTopicsVotedByUser(String userId, Integer pageNumber, Integer limit);
}
