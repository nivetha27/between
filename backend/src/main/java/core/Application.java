package core;

import core.entities.Topic;
import core.entities.User;
import core.exceptions.EntityNotFoundException;
import core.requests.*;
import core.responses.*;
import dataprovider.DataProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Optional;

public class Application {

  private final static Log logger = LogFactory.getLog(Application.class);

  private final DataProvider dataProvider;

  public Application(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
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

  public CreateTopicResponse createTopic(CreateTopicRequest createTopicRequest) {
    try {
      dataProvider.createTopic(createTopicRequest.toTopic());
      return new CreateTopicResponse.Builder(true).create();
    } catch (Exception e) {
      logger.error("CreateTopic failed for " + createTopicRequest.getTopicId(), e);
      return new CreateTopicResponse.Builder(false).create();
    }
  }

  public VoteTopicResponse voteChoice(VoteTopicRequest request) {
    try {
      dataProvider.voteTopic(request.toVote());
      return new VoteTopicResponse.Builder(true).create();
    } catch (Exception e) {
      logger.error("VoteTopic failed for " + request.getChoiceId(), e);
      return new VoteTopicResponse.Builder(false).create();
    }
  }

  public GetTopicResponse getTopic(GetTopicRequest request) {
    try {
      Topic topic = dataProvider.getTopic(request.getTopicId());
      return new GetTopicResponse.Builder(true, topic).create();
    } catch (Exception e) {
      logger.error("GetTopic failed for " + request.getTopicId(), e);
      return new GetTopicResponse.Builder(false, null).create();
    }
  }
}
