package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

import Common.APIDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemCarrito;
import Common.Logger;
import Common.ProductNotFoundException;
import Common.Usuario;

public class Conexion implements InterfazServidor {

	private Logger logger;
	private InterfazServidor servidor;
	public Conexion() {
		logger = new Logger(this.getClass().getSimpleName());
		connect();

	}

	public void connect() {
		Registry r = null;
		try {
			r = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
			servidor = (InterfazServidor) r.lookup("Servidor");
		} catch (RemoteException e) {
			logger.error("No se pudo conectar con el servidor principal: " + e.getMessage());
			connectToBackupServer(r);
		} catch (NotBoundException e) {
			logger.error("No se encontró 'Servidor' en el Registro");
			connectToBackupServer(r);
		}
		logger.log("Conectado al servidor");
	}


	public void connectToBackupServer(Registry r) {
		try {
			r = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1100);
			servidor = (InterfazServidor) r.lookup("ServidorRespaldo");
		} catch (RemoteException e) {
			logger.error("No se pudo conectar con el servidor de respaldo");
			System.exit(1);
		} catch (NotBoundException e) {
			logger.error("No se encontró 'ServidorRespaldo' en el Registro");
			System.exit(1);
		}
		logger.log("Servidor de respaldo encontrado");
	}

	public void reconnect() {
		logger.warning("Intentando reconectar..");
		connect();
	}

	@FunctionalInterface
	public interface RemoteOperation<T> {
		T execute() throws RemoteException;
	}


	public <T> T runWithBackup(RemoteOperation<T> operation) {
		logger.log("Espere un momento..");

		try {
			return operation.execute();
		} catch (RemoteException e) {
			logger.error("Error de conexión con el servidor principal: "+ e.getMessage());
			reconnect();
			try {
				return operation.execute();
			} catch (RemoteException e2) {
				logger.error("Error de conexión con el servidor de respaldo");
				throw new RuntimeException("No se pudo conectar con el servidor de respaldo");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error inesperado: "+ e.getMessage());
		}
	}

	public Usuario logIn(int id, int clave) throws RemoteException {
		return runWithBackup(() -> servidor.logIn(id, clave));
	}

	@Override
	public int generarBoleta(ArrayList<ItemCarrito> itemsCarrito, int idCajero) throws RemoteException {
		return runWithBackup(() -> servidor.generarBoleta(itemsCarrito, idCajero));
	}

	@Override
	public Item obtenerItem(int idProducto) throws RemoteException, APIDownException, ProductNotFoundException {
		return runWithBackup(() -> servidor.obtenerItem(idProducto));
	}

	@Override
	public Boleta obtenerBoleta(int idBoleta)
			throws RemoteException, BoletaNotFoundException, APIDownException {
				return runWithBackup(() -> servidor.obtenerBoleta(idBoleta));

	}

	@Override
	public void agregarStock(int id, int cantidad) throws RemoteException, ProductNotFoundException {
		runWithBackup(() -> {
			servidor.agregarStock(id, cantidad);
			return null;
		});
	}


	@Override
	public void eliminarStock(int id, int cantidad) throws RemoteException, ProductNotFoundException {
		runWithBackup(() -> {
			servidor.eliminarStock(id, cantidad);
			return null;
		});
	}
		

	@Override
	public int obtenerStock(int id) throws RemoteException, ProductNotFoundException {
		return runWithBackup(() -> servidor.obtenerStock(id));
	}

}
