package Common;

public class BoletaNotFoundException extends RuntimeException {
    public BoletaNotFoundException(int idBoleta) {
        super("Boleta N° " + Integer.toString(idBoleta) + " not found.");
    }
}
