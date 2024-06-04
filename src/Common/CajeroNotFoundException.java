package Common;

public class CajeroNotFoundException extends RuntimeException {
    public CajeroNotFoundException(int id) {
        super("Cajero con ID " + id + " no encontrado.");
    }
}
