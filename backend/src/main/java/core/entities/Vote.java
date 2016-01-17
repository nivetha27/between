package core.entities;

import org.joda.time.DateTime;

public class Vote {

  private String topicId;

  private String voteId;

  private String choiceId;

  private String userId;

  private String comment;

  private DateTime dateTime;

  public DateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(DateTime dateTime) {
    this.dateTime = dateTime;
  }

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

  public String getVoteId() {
    return voteId;
  }

  public void setVoteId(String voteId) {
    this.voteId = voteId;
  }

  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }
}
