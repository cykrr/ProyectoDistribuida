package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Administrador extends Cliente {

	public Administrador() throws RemoteException, NotBoundException {
		super();
	}

	public void menu(int idUsuario) {
		Scanner scanner = new Scanner(System.in);
		
		int id = 0;
		int cantidad = 0;
		
		while (true) {
			if(idUsuario == -1) {
				System.out.println("----El usuario " + idUsuario+ " a iniciado sesión----");
			}
			System.out.println("\nPor favor, elija una opción:");
			System.out.println("1. Añadir stock a producto");
			System.out.println("2. Eliminar stock a producto");
			System.out.println("3. Consultar Stock");
			System.out.println("9. Cerrar sesión");
			System.out.println("0. Salir");
			System.out.print("Opción: ");
			
			int opcion = scanner.nextInt();
			
			switch (opcion) {
			case 1:
				System.out.print("\nIngrese id del producto: ");
				id = scanner.nextInt();
				
				System.out.print("Ingrese cantidad: ");
				cantidad = scanner.nextInt();
				
				//carrito = caja.agregarItem(carrito, id, cantidad);
				break;
				
			case 2:
				System.out.print("\nIngrese id del producto: ");
				id = scanner.nextInt();
				
				System.out.print("Ingrese cantidad a eliminar: ");
				cantidad = scanner.nextInt();
				
				//carrito = caja.eliminarItem(carrito, id, cantidad);
				break;
				
			case 3:
				System.out.print("\nIngrese id del producto: ");
				id = scanner.nextInt();
				//caja.consultarItem(id);
				break;
				
				
			case 9:
				idUsuario = -1;
				System.out.print("Sesión cerrada\n");
				break;
				
				
			case 0:
				System.out.println("Saliendo...");
				scanner.close();
				System.exit(0);
			}
		}
	}

}
