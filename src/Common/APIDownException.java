package Common;

public class APIDownException extends RuntimeException {
    public APIDownException() {
        super("API is down.");
    }

}
