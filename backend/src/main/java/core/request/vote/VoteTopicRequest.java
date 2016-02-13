package core.request.vote;

import org.joda.time.DateTime;

import java.util.UUID;

public class VoteTopicRequest {

  private String topicId;

  private String choiceId;

  private String userId;

  private String comment;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  public core.entities.Vote toCoreVote() {
    core.entities.Vote vote = new core.entities.Vote();
    vote.setVoteId(UUID.randomUUID().toString());
    vote.setDateTime(DateTime.now());
    vote.setTopicId(getTopicId());
    vote.setChoiceId(getChoiceId());
    vote.setUserId(getUserId());
    vote.setComment(getComment());
    return vote;
  }
}
