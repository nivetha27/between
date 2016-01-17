package restservice.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.Application;
import core.entities.User;
import core.exceptions.EntityNotFoundException;
import core.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  Application application;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody User user) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(user.getUserId()), "userId is null or empty");
      application.createUser(user);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity get(@RequestParam(value = "id", required = true) String userId) {
    try {
      User user = application.getUser(userId);
      System.out.println(user.getUserId());
      System.out.println(user.getCategories());
      return new ResponseEntity(user, HttpStatus.ACCEPTED);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
