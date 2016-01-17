package core.requests;

import org.omg.PortableInterceptor.ServerRequestInfo;

public class GetTopicRequest {

  private String topicId;

  public GetTopicRequest(Builder builder) {
    this.topicId = builder.topicId;
  }

  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public static class Builder {

    private String topicId;

    public Builder(String topicId) {
      this.topicId = topicId;
    }

    public GetTopicRequest create() {
      return new GetTopicRequest(this);
    }
  }
}
