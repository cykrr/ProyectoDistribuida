package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Common.InterfazServidor;

public class Cliente {
	public Cliente() throws RemoteException, NotBoundException {
		
	}
	
	Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    InterfazServidor servidor = (InterfazServidor) registry.lookup("Servidor");
}
