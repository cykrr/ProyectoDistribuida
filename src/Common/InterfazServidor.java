package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfazServidor extends Remote {
    public void generarBoleta(ArrayList<ItemCarrito> itemsCarrito, int idCajero) throws RemoteException, SQLException;
    public Item obtenerItem(int idProducto) throws RemoteException, APIDownException, ProductNotFoundException;
    public Boleta obtenerBoleta(int idBoleta) throws RemoteException, SQLException;
    public Usuario logIn(int id, int clave) throws RemoteException, InvalidCredentialsException;
}
