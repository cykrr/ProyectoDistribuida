package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import Common.InterfazServidor;
import Common.Usuario;

public class Cliente {
	private InterfazServidor servidor;
	private Scanner scanner;
	
	private static final int ROL_CAJERO = 0;
	private static final int ROL_ADMIN = 1;
	
	public Cliente() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	    servidor = (InterfazServidor) registry.lookup("Servidor");
		scanner = new Scanner(System.in);
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
			
			if (usuario.getRol() == ROL_CAJERO) {
				Caja caja = new Caja(usuario, servidor, scanner);
				caja.mostrarMenu();
			} else if (usuario.getRol() == ROL_ADMIN) {
				Administrador administrador = new Administrador(usuario, servidor, scanner);
				administrador.mostrarMenu();
			}
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error con el servidor\n");
		}
	}
	
}
