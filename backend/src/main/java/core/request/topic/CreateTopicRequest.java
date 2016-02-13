package core.request.topic;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateTopicRequest {

  private String description;

  private String category;

  private List<Choice> choices;

  private String userId;

  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public core.entities.Topic toCoreTopic() {
    core.entities.Topic topic = new core.entities.Topic();
    topic.setTopicId(UUID.randomUUID().toString());
    topic.setDateTime(DateTime.now());
    topic.setDescription(getDescription());
    topic.setCategory(getCategory());
    topic.setUserId(getUserId());
    List<core.entities.Choice> coreChoices = new ArrayList<>();
    int counter = 0;
    for (Choice choice : getChoices()) {
      coreChoices.add(choice.toCoreChoice(++counter));
    }
    topic.setChoices(new ArrayList<>(coreChoices));
    return topic;
  }
}
