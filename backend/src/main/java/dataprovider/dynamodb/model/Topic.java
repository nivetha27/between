package dataprovider.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import core.entities.*;
import core.entities.Choice;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.amazonaws.util.Throwables.failure;

@DynamoDBTable(tableName = "Topic")
public class Topic {

  public static class ChoiceMarshaller extends JsonMarshaller<List<core.entities.Choice>> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writer();

    @Override
    public String marshall(List<Choice> obj) {

      try {
        return writer.writeValueAsString(obj);
      } catch (JsonProcessingException e) {
        throw failure(e,
            "Unable to marshall the instance of " + obj.getClass()
                + "into a string");
      }
    }

    @Override
    public List<Choice> unmarshall(Class<List<Choice>> clazz, String json) {
      try {
        return mapper.readValue(json, new TypeReference<List<Choice>>(){});
      } catch (Exception e) {
        throw failure(e, "Unable to unmarshall the string " + json
            + "into " + clazz);
      }
    }
  }

  private String topicId;

  private String description;

  private String category;

  private String dateTime;

  private List<Choice> choices;

  private String userId;

  @DynamoDBAttribute(attributeName = "userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @DynamoDBAttribute(attributeName = "choices")
  @DynamoDBMarshalling(marshallerClass = ChoiceMarshaller.class)
  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  @DynamoDBHashKey(attributeName = "topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  @DynamoDBAttribute(attributeName = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @DynamoDBAttribute(attributeName = "category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @DynamoDBAttribute(attributeName = "datetime")
  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public static Topic fromTopic(core.entities.Topic topic) {
    Topic topicModel = new Topic();
    topicModel.setTopicId(topic.getTopicId());
    topicModel.setDescription(topic.getDescription());
    topicModel.setCategory(topic.getCategory());
    topicModel.setChoices(new ArrayList<Choice>(topic.getChoices()));
    topicModel.setDateTime(topic.getDateTime().toString());
    topicModel.setUserId(topic.getUserId());
    return topicModel;
  }

  public core.entities.Topic toTopic() {
    core.entities.Topic topic = new core.entities.Topic();
    topic.setTopicId(getTopicId());
    topic.setDescription(getDescription());
    topic.setCategory(getCategory());
    topic.setChoices(new ArrayList<Choice>(getChoices()));
    topic.setDateTime(DateTime.parse(getDateTime()));
    topic.setUserId(getUserId());
    return topic;
  }
}
