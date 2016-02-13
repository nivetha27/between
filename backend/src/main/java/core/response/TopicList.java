package core.response;

import core.entities.Topic;

import java.util.List;

public class TopicList {

  private List<Topic> topics;

  private String lastEvaluatedKey;

  public List<Topic> getTopics() {
    return topics;
  }

  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }

  public String getLastEvaluatedKey() {
    return lastEvaluatedKey;
  }

  public void setLastEvaluatedKey(String lastEvaluatedKey) {
    this.lastEvaluatedKey = lastEvaluatedKey;
  }
}
