package Server;

public class ProductNotFoundException extends RuntimeException {
    ProductNotFoundException(int id) {
        super("Product with id " + id + " not found.");
    }

}
