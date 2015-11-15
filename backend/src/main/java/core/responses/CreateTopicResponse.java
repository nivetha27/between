package core.responses;

public class CreateTopicResponse {

  private final boolean isSuccess;

  public CreateTopicResponse(Builder builder) {
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

    public CreateTopicResponse create() {
      return new CreateTopicResponse(this);
    }
  }
}
