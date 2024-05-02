package Server;

public class APIDownException extends RuntimeException {
    APIDownException() {
        super("API is down.");
    }

}
