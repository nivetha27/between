package restservice.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.Application;
import core.exceptions.EntityNotFoundException;
import core.request.user.CreateUserRequest;
import core.request.user.UpdateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  @Autowired
  Application application;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody CreateUserRequest createUserRequest) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(createUserRequest.getUserId()), "userId cannot be null or empty");
      application.createUser(createUserRequest.toCoreUser());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      LOG.error("CreateUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity get(@RequestParam(value = "id", required = true) String userId) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(userId), "userId cannot be null or empty");
      core.entities.User user = application.getUser(userId);
      return new ResponseEntity(user, HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      LOG.error("GetUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity delete(@RequestParam(value = "id", required = true) String userId) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(userId), "userId cannot be null or empty");
      application.deleteUser(userId);
      return new ResponseEntity(HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      LOG.error("DeleteUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity update(@RequestBody UpdateUserRequest updateUserRequest) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(updateUserRequest.getUserId()), "userId cannot be null or empty");
      application.updateUser(updateUserRequest.toCoreUser());
      return new ResponseEntity(HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      LOG.error("UpdateUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
