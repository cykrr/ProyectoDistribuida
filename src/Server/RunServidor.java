package Server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Common.InterfazServidor;

public class RunServidor {
    public static void main(String[] args) throws IOException {
        try {
            InterfazServidor servidor = new Servidor();
            Registry r = LocateRegistry.createRegistry(1099);
            r.bind("Servidor", (Remote) servidor);
        } catch (RemoteException e) {
            System.err.println("Error al crear el servidor (RemoteException)");
        } catch (AlreadyBoundException e) {
            System.err.println("Error al crear el servidor");
            System.err.println("El puerto 1099 ya está en uso.");
        }
        System.out.println("Servidor inicializado correctamente.");
    }

}
