package game.kalaha.exception;

public class PitNotFoundException extends Exception {
	  public PitNotFoundException() { super(); }
	  public PitNotFoundException(String message) { super(message); }
	  public PitNotFoundException(String message, Throwable cause) { super(message, cause); }
	  public PitNotFoundException(Throwable cause) { super(cause); }
}
