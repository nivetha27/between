package core.requests;

import core.entities.User;

import java.util.ArrayList;
import java.util.List;

public final class CreateUserRequest {

  private final String userId;
  private final List<String> categories;

  public CreateUserRequest(Builder builder) {
    this.userId = builder.userId;
    this.categories = builder.categories;
  }

  public String getUserId() {
    return userId;
  }

  public List<String> getCategories() {
    return categories;
  }

  public User toUser() {
    User user = new User();
    user.setUserId(getUserId());
    user.setCategories(getCategories());
    return user;
  }

  public static class Builder {

    private String userId;
    private List<String> categories = new ArrayList<>();

    public Builder(String userId) {
      this.userId = userId;
    }

    public Builder withCategories(List<String> categories) {
      this.categories = categories;
      return this;
    }

    public CreateUserRequest create() {
      return new CreateUserRequest(this);
    }
  }
}
