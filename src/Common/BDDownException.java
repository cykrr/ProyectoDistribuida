package Common;

public class BDDownException extends RuntimeException {
    public BDDownException() {
        super("Database is down.");
    }
}
