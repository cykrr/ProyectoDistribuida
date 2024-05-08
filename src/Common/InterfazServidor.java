package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfazServidor extends Remote {
    public int generarBoleta(ArrayList<ItemCarrito> itemsCarrito, int idCajero) throws RemoteException, SQLException;
    public Item obtenerItem(int idProducto) throws RemoteException, APIDownException, ProductNotFoundException;
    public Boleta obtenerBoleta(int idBoleta) throws RemoteException, BoletaNotFoundException, SQLException, APIDownException;
    public Usuario logIn(int id, int clave) throws RemoteException, InvalidCredentialsException;
    public void agregarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException;
    public void eliminarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException;
    public int obtenerStock(int id) throws RemoteException, SQLException, ProductNotFoundException;
}
