package Common;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(int message) {
        super("Invalid credentials for user with id: " + message);
    }
}
