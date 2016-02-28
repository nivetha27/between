package core.exceptions;

public class EntityTooLargeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public EntityTooLargeException(String message) {
    super(message);
  }
}
