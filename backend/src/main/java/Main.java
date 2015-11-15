import core.Application;
import core.entities.User;
import core.requests.CreateUserRequest;
import core.responses.CreateUserResponse;
import dataprovider.DataProvider;
import dataprovider.dynamodb.DynamoDBDataProviderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    DataProvider dataProvider = DynamoDBDataProviderFactory.create();
    Application application = new Application(dataProvider);
    List<String> categories = Arrays.asList("category1", "category2");
    CreateUserRequest createUserRequest = new CreateUserRequest.Builder("id1")
        .withCategories(categories)
        .create();
    CreateUserResponse createUserResponse = application.createUser(createUserRequest);
    System.out.println(createUserResponse.isSuccess());
  }
}
