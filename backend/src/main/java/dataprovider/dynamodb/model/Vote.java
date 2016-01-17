package dataprovider.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.joda.time.DateTime;

@DynamoDBTable(tableName = "Vote")
public class Vote {

  private String voteId;

  private String userId;

  private String topicId;

  private String choiceId;

  private String comment;

  private String dateTime;

  @DynamoDBAttribute(attributeName = "dateTime")
  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  @DynamoDBHashKey(attributeName = "voteId")
  public String getVoteId() {
    return voteId;
  }

  public void setVoteId(String voteId) {
    this.voteId = voteId;
  }

  @DynamoDBAttribute(attributeName = "topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  @DynamoDBAttribute(attributeName = "userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @DynamoDBAttribute(attributeName = "choiceId")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  @DynamoDBAttribute(attributeName = "comment")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public static Vote fromVote(core.entities.Vote vote) {
    Vote voteModel = new Vote();
    voteModel.setUserId(vote.getVoteId());
    voteModel.setChoiceId(vote.getChoiceId());
    voteModel.setComment(vote.getComment());
    voteModel.setVoteId(vote.getVoteId());
    voteModel.setTopicId(vote.getTopicId());
    voteModel.setDateTime(vote.getDateTime().toString());
    return voteModel;
  }

  public core.entities.Vote toVote() {
    core.entities.Vote vote = new core.entities.Vote();
    vote.setUserId(getUserId());
    vote.setChoiceId(getChoiceId());
    vote.setComment(getComment());
    vote.setTopicId(getTopicId());
    vote.setVoteId(getVoteId());
    vote.setDateTime(DateTime.parse(getDateTime()));
    return vote;
  }
}
