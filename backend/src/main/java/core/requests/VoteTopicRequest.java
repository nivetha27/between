package core.requests;

import core.entities.Vote;
import org.joda.time.DateTime;

import java.util.UUID;

public class VoteTopicRequest {

  final private String topicId;

  final private String choiceId;

  final private String voteId;

  final private String userId;

  final private String comment;

  final private DateTime dateTime;

  public VoteTopicRequest(Builder builder) {
    this.choiceId = builder.choiceId;
    this.topicId = builder.topicId;
    this.userId = builder.userId;
    this.voteId = builder.voteId;
    this.comment = builder.comment;
    this.dateTime = builder.dateTime;
  }

  public String getTopicId() {
    return topicId;
  }

  public String getChoiceId() {
    return choiceId;
  }

  public String getVoteId() {
    return voteId;
  }

  public String getUserId() {
    return userId;
  }

  public String getComment() {
    return comment;
  }

  public Vote toVote() {
    Vote vote = new Vote();
    vote.setChoiceId(choiceId);
    vote.setTopicId(topicId);
    vote.setUserId(userId);
    vote.setVoteId(voteId);
    vote.setComment(comment);
    vote.setDateTime(dateTime);
    return vote;
  }

  public static class Builder {

    private String topicId;

    private String choiceId;

    private String voteId = UUID.randomUUID().toString();

    private String userId;

    private String comment = "";

    private DateTime dateTime = DateTime.now();

    public Builder(String topicId, String choiceId, String userId) {
      this.topicId = topicId;
      this.choiceId = choiceId;
      this.userId = userId;
    }

    public Builder withVoteId(String voteId) {
      this.voteId = voteId;
      return this;
    }

    public Builder withComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Builder withDateTime(DateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public VoteTopicRequest create() {
      return new VoteTopicRequest(this);
    }
  }
}
