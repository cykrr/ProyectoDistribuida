package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Scanner;

import Common.InterfazServidor;
import Common.Logger;
import Common.Rol;
import Common.Usuario;


public class Cliente {
	private Scanner scanner;
	Registry r = null;
	InterfazServidor servidor = null;
	Logger logger = new Logger(this.getClass().getSimpleName());
	
	public Cliente() {

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

		scanner = new Scanner(System.in);
		startClient();
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

	
	public void startClient() {
		int opcion;

		do {
			System.out.println("Por favor, elige una opción:\n");
			System.out.println("1. Iniciar sesión");
			System.out.println("2. Salir\n");
			System.out.print("Opción: ");
			
			opcion = scanner.nextInt();
			System.out.println();
			
			switch (opcion) {
				case 1:
					iniciarSesion();
					break;
				case 2:
					break;
				default:
					System.out.println("Opción inválida\n");
			}
					
		} while(opcion != 2);
		
		System.out.println("Saliendo...");
		scanner.close();

	}
	
	private void iniciarSesion() {	
		System.out.print("Ingrese id: ");
		int id = scanner.nextInt();
		
		System.out.print("Ingrese clave: ");
		int clave = scanner.nextInt();
		
		try {
			Usuario usuario = servidor.logIn(id, clave);
			if (usuario == null) {
				System.out.println("Credenciales incorrectas\n");
				return;
			}
			System.out.println();
			
			if (usuario.getRol() == Rol.CAJERO) {
				Caja caja = new Caja(usuario, servidor, scanner);
				caja.mostrarMenu();
			} else if (usuario.getRol() == Rol.ADMIN) {
				Administrador administrador = new Administrador(usuario, servidor, scanner);
				administrador.mostrarMenu();
			}
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error con el servidor\n");
		} catch (SQLException e) {
			System.out.println("Ocurrió un error con la base de datos\n");
		}
	}
	
}
