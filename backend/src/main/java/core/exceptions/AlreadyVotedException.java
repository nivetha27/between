package core.exceptions;

public class AlreadyVotedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AlreadyVotedException(String topicId, String userId) {
    super("TopicId: " + topicId + " already voted by userId: " + userId);
  }
}
