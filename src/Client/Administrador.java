package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import Common.Item;

public class Administrador extends Cliente {

	public Administrador() throws RemoteException, NotBoundException {
		super();
	}

	public void menu(int idUsuario) throws RemoteException {
		Scanner scanner = new Scanner(System.in);
		
		int id = 0;
		int cantidad = 0;
		
		while (idUsuario !=- 1) {
			System.out.println("----El administrador " + idUsuario+ " a iniciado sesión----");
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
					while(cantidad <=0) {
						System.out.print("Ingrese valores mayores a 0 \nIntente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					
					//Método para módificar stock
					break;
					
				case 2:
					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad a eliminar: ");
					cantidad = scanner.nextInt();
					while(cantidad <=0) {
						System.out.print("Ingrese valores mayores a 0 \nIntente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					break;
					
				case 3:
					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					Item item = servidor.obtenerItem(id);
					System.out.println("\nNombre:" + item.getNombre());
					System.out.println("Cantidad disponible: " + item.getCantidadPack());
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
