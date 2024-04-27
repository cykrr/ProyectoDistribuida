package Server;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

import Common.InterfazServidor;

public class Servidor implements InterfazServidor    {

	Servidor() throws RemoteException {
		super();
		UnicastRemoteObject.exportObject(this, 0);
		// TODO Auto-generated constructor stub
	}

}
