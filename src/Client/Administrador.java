package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Administrador extends Cliente {

	public Administrador() throws RemoteException, NotBoundException {
		super();
		// TODO Auto-generated constructor stub
		//Administrador admin = new Administrador();
		
		//Caja caja = new Caja();
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println("\nPor favor, elige una opción:");
			System.out.println("1. Consultar Stock");
			System.out.println("2. Añadir Producto");
			System.out.println("3. Modificar Stock");
			System.out.println("4. Salir");
			System.out.print("Opción: ");
			
			int opcion = scanner.nextInt();
			
			//int id;
			//int cantidad;
			switch (opcion) {
			
				case 1:
					break;
					
				case 2:
					break;
					
				case 3:
					break;
				case 4:
					System.out.println("Saliendo...");
					scanner.close();
					System.exit(0);
			}
		}
	}

}
