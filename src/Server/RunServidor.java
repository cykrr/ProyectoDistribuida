package Server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Common.InterfazServidor;

public class RunServidor {
    public static void main(String[] args) {
        System.out.println("Initializing server...");
        InterfazServidor servidor = null;
        Registry r = null;
        try {
            servidor = new Servidor();
        } catch (IOException e) {
            System.err.println("IOException occurred while creating the server.");
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        
        try {
            r = LocateRegistry.createRegistry(1099);
            r.bind("Servidor", (Remote) servidor);
        } catch (RemoteException e) { 
            System.err.println("RemoteException occurred while creating the registry.");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.err.println("Server already bound.");
            e.printStackTrace();
        }

        System.out.println("Server initialized successfully.");
    }


}
