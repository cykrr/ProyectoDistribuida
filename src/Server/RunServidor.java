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
        try {
            InterfazServidor servidor = new Servidor();
            Registry r = LocateRegistry.createRegistry(1099);
            r.bind("Servidor", (Remote) servidor);
        } catch (RemoteException e) {
            System.err.println("RemoteException occurred while creating the server.");
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.err.println("AlreadyBoundException occurred while creating the server.");
            System.err.println("The port 1099 is already in use.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IOException occurred while creating the server.");
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (APIDownException e) { 
            System.err.println("WARNING: API is down. Server will not be able to function properly.");
        } catch (BDDownException e) {
            System.err.println("WARNING: Database is down. Server will not be able to function properly.");
        }
        System.out.println("Server initialized successfully.");
    }


}
