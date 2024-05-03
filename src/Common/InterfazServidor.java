package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface InterfazServidor extends Remote {
    int enviarBoleta(Boleta boleta) throws RemoteException;
    Item obtenerItem(int idProducto) throws RemoteException;
    Boleta obtenerBoleta(int idBoleta) throws RemoteException, SQLException;
}
