package Common;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(int id) {
        super("Producto con ID " + id + " no ha sido encontrado.");
    }

}
