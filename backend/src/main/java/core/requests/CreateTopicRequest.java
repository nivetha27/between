package core.requests;

import core.entities.Choice;
import core.entities.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateTopicRequest {

  private final String topicId;

  private final String question;

  private final List<String> imageUrls;

  private final String category;

  public CreateTopicRequest(Builder builder) {
    this.question = builder.question;
    this.imageUrls = builder.imageUrls;
    this.category = builder.category;
    this.topicId = builder.topicId;
  }

  public Topic toTopic() {
    Topic topic = new Topic();
    topic.setQuestion(question);
    topic.setCategory(category);
    topic.setTopicId(topicId);
    return topic;
  }

  public List<Choice> toChoices() {
    List<Choice> choices = new ArrayList<>();
    for (String imageUrl : imageUrls) {
      Choice choice = new Choice();
      choice.setTopicId(topicId);
      choice.setChoiceId(UUID.randomUUID().toString());
      choice.setImageUrl(imageUrl);
      choices.add(choice);
    }
    return choices;
  }

  public static class Builder {

    private String topicId = UUID.randomUUID().toString();

    private String question;

    private List<String> imageUrls;

    private String category = "";

    public Builder(String question, List<String> imageUrls, String category) {
      this.question = question;
      this.imageUrls = new ArrayList<>(imageUrls);
    }

    public Builder withCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder withTopicId(String topicId) {
      this.topicId = topicId;
      return this;
    }

    public CreateTopicRequest create() {
      return new CreateTopicRequest(this);
    }
  }
}
