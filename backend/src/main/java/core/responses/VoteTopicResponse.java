package core.responses;

public class VoteTopicResponse {

  private final boolean isSuccess;

  public VoteTopicResponse(Builder builder) {
    this.isSuccess = builder.isSuccess;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public static class Builder {
    private final boolean isSuccess;

    public Builder(boolean isSuccess) {
      this.isSuccess = isSuccess;
    }

    public VoteTopicResponse create() {
      return new VoteTopicResponse(this);
    }
  }
}
