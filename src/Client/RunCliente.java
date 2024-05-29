package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunCliente {
	
	public static void main(String[] args) {
		try {
			Cliente cliente = new Cliente();
			cliente.startClient();
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Ocurrió un error al iniciar el cliente");
		}
	}

}
