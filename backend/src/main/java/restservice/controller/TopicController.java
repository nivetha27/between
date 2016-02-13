package restservice.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.Application;
import core.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.request.topic.Choice;
import core.request.topic.CreateTopicRequest;

@RestController
@RequestMapping("/topic")
public class TopicController {

  private static final Logger LOG = LoggerFactory.getLogger(TopicController.class);

  @Autowired
  Application application;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody CreateTopicRequest createTopicRequest) {
    try {
      validateTopic(createTopicRequest);
      application.createTopic(createTopicRequest.toCoreTopic());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      LOG.error("CreateTopic failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity delete(@RequestParam(value = "id", required = true) String topicId) {
    try {
      Preconditions.checkState(!Strings.isNullOrEmpty(topicId), "topicId should not be null or empty");
      application.deleteTopic(topicId);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      LOG.error("CreateTopic failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void validateTopic(CreateTopicRequest createTopicRequest) {
    Preconditions.checkState(!Strings.isNullOrEmpty(createTopicRequest.getDescription()), "Topic description should not be null or empty");
    Preconditions.checkState(!Strings.isNullOrEmpty(createTopicRequest.getCategory()), "Topic category should not be null or empty");
    Preconditions.checkState(!Strings.isNullOrEmpty(createTopicRequest.getUserId()), "userId should not be null or empty");
    Preconditions.checkState(createTopicRequest.getChoices().size() == 2, "Exactly two choices should be present");
    validateChoice(createTopicRequest.getChoices().get(0));
    validateChoice(createTopicRequest.getChoices().get(1));
  }

  private void validateChoice(Choice choice) {
    Preconditions.checkState(!Strings.isNullOrEmpty(choice.getCaption()), "choice caption should not be null or empty");
    Preconditions.checkState(!Strings.isNullOrEmpty(choice.getImageUrl()), "choice imageUrl should not be null or empty");
  }
}
