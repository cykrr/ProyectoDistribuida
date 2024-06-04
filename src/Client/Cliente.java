package Client;

import java.rmi.RemoteException;
import java.util.Scanner;

import Common.InterfazServidor;
import Common.Rol;
import Common.Usuario;


public class Cliente {
	private Scanner scanner;
	private Conexion conexion;
	
	
	public Cliente() {
		conexion = new Conexion();
		scanner = new Scanner(System.in);
		startClient();
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
			Usuario usuario = conexion.logIn(id, clave);
			if (usuario == null) {
				System.out.println("Credenciales incorrectas\n");
				return;
			}
			System.out.println();
			
			if (usuario.getRol() == Rol.CAJERO) {
				Caja caja = new Caja(usuario, conexion, scanner);
				caja.mostrarMenu();
			} else if (usuario.getRol() == Rol.ADMIN) {
				Administrador administrador = new Administrador(usuario, conexion, scanner);
				administrador.mostrarMenu();
			}
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error con el servidor\n");
		}
	}
	
}
