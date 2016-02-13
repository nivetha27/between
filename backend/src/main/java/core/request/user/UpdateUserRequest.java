package core.request.user;

import java.util.List;

public class UpdateUserRequest {

  private String userId;

  private List<String> categories;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public core.entities.User toCoreUser() {
    core.entities.User user = new core.entities.User();
    user.setUserId(getUserId());
    user.setCategories(getCategories());
    return user;
  }
}
