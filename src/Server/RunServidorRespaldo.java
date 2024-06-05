package Server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Common.Logger;


public class RunServidorRespaldo {
    public static void main(String[] args) {
        Servidor servidor=null;
        Registry r;
        Logger logger = new Logger("RunServidorRespaldo");

        try {
            servidor = new Servidor();
        } catch (IOException e) { 
            System.out.println("Ocurrió un error al registrar el servidor de respaldo");
            System.exit(1);
        }

        try {
            r = LocateRegistry.createRegistry(1100);
            r.bind("ServidorRespaldo", (Remote) servidor);
        } catch (RemoteException e) {
            logger.error("Ocurrió un error al registrar el servidor de respaldo");
        } catch (AlreadyBoundException e) {
            logger.error("El servidor de respaldo ya está registrado");
        }
        logger.log("Servidor de respaldo registrado con éxito");

    }
    
}
