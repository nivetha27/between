package core;

import core.requests.CreateUserRequest;
import core.responses.CreateUserResponse;
import dataprovider.DataProvider;

public class Application {

  private final DataProvider dataProvider;

  public Application(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
    try {
      dataProvider.createUser(createUserRequest.toUser());
      return new CreateUserResponse.Builder(true).create();
    } catch (Exception e) {
      // todo logging
      return new CreateUserResponse.Builder(false).create();
    }
  }
}
