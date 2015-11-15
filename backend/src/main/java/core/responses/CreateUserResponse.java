package core.responses;

import core.requests.CreateUserRequest;

public class CreateUserResponse {

  private final boolean isSuccess;

  public CreateUserResponse(Builder builder) {
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

    public CreateUserResponse create() {
      return new CreateUserResponse(this);
    }
  }
}
