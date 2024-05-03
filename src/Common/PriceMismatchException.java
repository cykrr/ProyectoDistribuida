package Common;

public class PriceMismatchException extends RuntimeException {
    public PriceMismatchException(int idProduct, double dbPrice, double boletaPrice) {
        super("Price of product with id " + idProduct + " does not match the price in the database\n Got:" +
         Double.toString(boletaPrice) + "\n Expected:" + Double.toString(dbPrice));
    }
}
