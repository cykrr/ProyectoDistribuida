package Common;

public class StockMismatchException extends RuntimeException {
    public StockMismatchException(int idProduct, int dbStock) {
        super("Stock of product with id " + idProduct + " does not have enough stock to sell it\n Got:" +
         Integer.toString(dbStock) + "\n Expected: > 0");
    }
}
