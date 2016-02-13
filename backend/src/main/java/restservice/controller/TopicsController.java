package restservice.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.Application;
import core.entities.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicsController {

  private static final Logger LOG = LoggerFactory.getLogger(TopicsController.class);

  private final static String LIMIT = "20";

  @Autowired
  Application application;

  @RequestMapping(value = "/topics/category", method = RequestMethod.GET)
  public ResponseEntity listTopicsByCategory(@RequestParam(value = "category", required = true) String category,
                                             @RequestParam(value = "page", required = true) Integer pageNumber,
                                             @RequestParam(value = "limit", required = false, defaultValue = LIMIT) Integer limit) {
    try {
      Preconditions.checkState(pageNumber >= 1, "page value should be greater than 1");
      Preconditions.checkState(!Strings.isNullOrEmpty(category), "category should not be null or empty");
      Preconditions.checkState(limit >= 1 && limit <= 20, "limit value should be between 1 and 20");
      List<Topic> topics = application.listTopicsByCategory(category, pageNumber, limit);
      return new ResponseEntity(topics, HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      LOG.error("ListTopicsByCategory failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/topics/user/created", method = RequestMethod.GET)
  public ResponseEntity listTopicsCreatedByUser(@RequestParam(value = "user_id", required = true) String userId,
                                             @RequestParam(value = "page", required = true) Integer pageNumber,
                                             @RequestParam(value = "limit", required = false, defaultValue = LIMIT) Integer limit) {
    try {
      Preconditions.checkState(pageNumber >= 1, "page value should be greater than 1");
      Preconditions.checkState(!Strings.isNullOrEmpty(userId), "userId should not be null or empty");
      Preconditions.checkState(limit >= 1 && limit <= 20, "limit value should be between 1 and 20");
      List<Topic> topics = application.listTopicsCreatedByUser(userId, pageNumber, limit);
      return new ResponseEntity(topics, HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      LOG.error("ListTopicsCreatedByUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/topics/user/voted", method = RequestMethod.GET)
  public ResponseEntity listTopicsVotedByUser(@RequestParam(value = "user_id", required = true) String userId,
                                                @RequestParam(value = "page", required = true) Integer pageNumber,
                                                @RequestParam(value = "limit", required = false, defaultValue = LIMIT) Integer limit) {
    try {
      Preconditions.checkState(pageNumber >= 1, "page value should be greater than 1");
      Preconditions.checkState(!Strings.isNullOrEmpty(userId), "userId should not be null or empty");
      Preconditions.checkState(limit >= 1 && limit <= 20, "limit value should be between 1 and 20");
      List<Topic> topics = application.listTopicsVotedByUser(userId, pageNumber, limit);
      return new ResponseEntity(topics, HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      LOG.error("ListTopicsVotedByUser failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
