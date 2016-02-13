package restservice.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.Application;
import core.exceptions.AlreadyVotedException;
import core.request.vote.VoteTopicRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {

  private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

  @Autowired
  Application application;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity voteTopic(@RequestBody VoteTopicRequest voteTopicRequest) {
    try {
      validateVote(voteTopicRequest);
      application.voteTopic(voteTopicRequest.toCoreVote());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalStateException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (AlreadyVotedException e) {
      return new ResponseEntity(HttpStatus.ALREADY_REPORTED);
    } catch (Exception e) {
      LOG.error("VoteTopic failed", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void validateVote(VoteTopicRequest voteTopicRequest) {
    Preconditions.checkState(!Strings.isNullOrEmpty(voteTopicRequest.getUserId()), "userId should not be null or empty");
    Preconditions.checkState(!Strings.isNullOrEmpty(voteTopicRequest.getTopicId()), "topicId should not be null or empty");
    Preconditions.checkState(!Strings.isNullOrEmpty(voteTopicRequest.getChoiceId()), "choiceId should not be null or empty");
  }
}
