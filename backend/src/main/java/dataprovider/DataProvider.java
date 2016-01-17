package dataprovider;

import core.entities.Topic;
import core.entities.User;
import core.entities.Vote;

import java.util.Optional;

public interface DataProvider {

  void createUser(User user);

  Optional<User> getUser(String userId);

  void createTopic(Topic topic);

  void voteTopic(Vote vote);

  void incrementVote(String topicId, Integer choiceId, long numVotes);

  Topic getTopic(String topicId);
}
