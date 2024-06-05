package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


public interface InterfazServidor extends Remote {
    public Usuario logIn(int id, int clave) throws RemoteException, InvalidCredentialsException, SQLException;
    public int generarBoleta(ArrayList<ItemCarrito> itemsCarrito, String nombreCajero) throws RemoteException, SQLException;
    public Item obtenerItem(int idProducto) throws RemoteException, APIDownException, ProductNotFoundException, SQLException;
    public Boleta obtenerBoleta(int idBoleta) throws RemoteException, BoletaNotFoundException, APIDownException, SQLException;
    public void agregarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException;
    public void eliminarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException;
    public int obtenerStock(int id) throws RemoteException, SQLException, ProductNotFoundException;
    public ArrayList<Usuario> obtenerCajeros() throws RemoteException, SQLException;
    public void agregarCajero(String nombre, int clave) throws RemoteException, SQLException;
    public void eliminarCajero(int id) throws RemoteException, SQLException, CajeroNotFoundException;
    public boolean requestMutex() throws RemoteException;
    public void releaseMutex() throws RemoteException;
}
