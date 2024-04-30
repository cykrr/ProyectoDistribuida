package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazServidor extends Remote {
    int enviarBoleta(Boleta boleta) throws RemoteException;
    int obtenerPrecio(int idProducto) throws RemoteException;
    int obtenerBoleta(int idBoleta) throws RemoteException;

}
