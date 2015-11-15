package dataprovider.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "User")
public class User {

  private String userId;

  private List<String> categories;

  @DynamoDBHashKey(attributeName = "userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @DynamoDBAttribute(attributeName = "categories")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public static User fromUser(core.entities.User user) {
    User userModel = new User();
    userModel.setUserId(user.getUserId());
    userModel.setCategories(user.getCategories());
    return userModel;
  }
}
