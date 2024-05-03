package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface InterfazServidor extends Remote {
    public void generarBoleta(Boleta boleta) throws RemoteException, SQLException;
    public Item obtenerItem(int idProducto) throws RemoteException;
    public Boleta obtenerBoleta(int idBoleta) throws RemoteException, SQLException;
}
