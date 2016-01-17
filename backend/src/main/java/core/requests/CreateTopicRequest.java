package core.requests;

import core.entities.Choice;
import core.entities.Topic;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateTopicRequest {

  private final String topicId;

  private final String description;

  private final List<Choice> choices;

  private final String category;

  private final DateTime dateTime;

  public static class Choice {
    final private String caption;
    final private String imageUrl;

    public Choice(String caption, String imageUrl) {
      this.caption = caption;
      this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public String getCaption() {
      return caption;
    }
  }

  public CreateTopicRequest(Builder builder) {
    this.description = builder.description;
    this.choices = builder.choices;
    this.category = builder.category;
    this.topicId = builder.topicId;
    this.dateTime = builder.dateTime;
  }

  public String getTopicId() {
    return topicId;
  }

  public String getDescription() {
    return description;
  }

  public List<Choice> getImageUrls() {
    return choices;
  }

  public String getCategory() {
    return category;
  }

  public Topic toTopic() {
    Topic topic = new Topic();
    topic.setDescription(description);
    topic.setCategory(category);
    topic.setTopicId(topicId);
    topic.setChoices(toChoices());
    topic.setDateTime(dateTime);
    return topic;
  }

  public List<core.entities.Choice> toChoices() {
    List<core.entities.Choice> choiceEntities = new ArrayList<>();
    Integer counter = 0;
    for (Choice choice : this.choices) {
      core.entities.Choice choiceEntity = new core.entities.Choice();
      choiceEntity.setChoiceId((++counter));
      choiceEntity.setCaption(choice.getCaption());
      choiceEntity.setImageUrl(choice.getImageUrl());
      choiceEntity.setVotes(0);
      choiceEntities.add(choiceEntity);
    }
    return choiceEntities;
  }

  public static class Builder {

    private String topicId = UUID.randomUUID().toString();

    private String description;

    private List<Choice> choices;

    private String category = "";

    private DateTime dateTime = DateTime.now();

    public Builder(String description, List<Choice> choices) {
      this.description = description;
      this.choices = new ArrayList<>(choices);
    }

    public Builder withCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder withTopicId(String topicId) {
      this.topicId = topicId;
      return this;
    }

    public Builder withDateTime(DateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public CreateTopicRequest create() {
      return new CreateTopicRequest(this);
    }
  }
}
