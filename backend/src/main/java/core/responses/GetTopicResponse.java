package core.responses;

import core.entities.Topic;

public class GetTopicResponse {

  private final Topic topic;

  private final boolean isSuccess;

  public GetTopicResponse(Builder builder) {
    this.isSuccess = builder.isSuccess;
    this.topic = builder.topic;
  }

  public Topic getTopic() {
    return topic;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public static class Builder {

    private Topic topic;

    private boolean isSuccess;

    public Builder(boolean isSuccess, Topic topic) {
      this.isSuccess = isSuccess;
      this.topic = topic;
    }

    public GetTopicResponse create() {
      return new GetTopicResponse(this);
    }
  }
}
