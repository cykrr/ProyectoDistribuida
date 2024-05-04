package Client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.Item;
import Common.ItemCarrito;


public class RunCaja {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
		
		Caja caja = null;
		Administrador admin = null;
		
        int idUsuario = -1;
        boolean esAdmin = false;
        
		ArrayList<ItemCarrito> carrito = new ArrayList<>();
		
		////////Variables para leer/////
		
		int id = 0;
		int cantidad = 0;
		int clave = 0;
		
		/////////////////////////////////
		
		Scanner scanner = new Scanner(System.in);
		
		while (idUsuario == -1) {
			System.out.println("Inicio de sesión");
			System.out.println("Ingrese id:");
			id = scanner.nextInt();
			
			System.out.print("Ingrese clave: ");
			clave = scanner.nextInt();
			
			//Validación de admin provisional, hay que cambiarlo
			if(id == 3) {
				esAdmin = true;
			}
			//Pendiente: Validar si existe usuario
			
			idUsuario = id;
		}
		
		if(!esAdmin) {
	        try {
	        	caja = new Caja();
	        }catch(RemoteException e) {
	        	System.out.print("No se encontró el servidor");
	        }
		}else {
			try {
	        	admin = new Administrador();
	        }catch(RemoteException e) {
	        	System.out.print("No se encontró el servidor");
	        }
		}
		
		while (true) {
			System.out.println("\nPor favor, elige una opción:");
			System.out.println("1. Agregar producto");
			System.out.println("2. Consultar producto");
			System.out.println("3. Eliminar producto");
			System.out.println("4. Consultar carrito");
			System.out.println("5. Finalizar venta");
			System.out.println("6. Salir");
			System.out.print("Opción: ");
			
			int opcion = scanner.nextInt();
			
			switch (opcion) {
			
				case 1:
					System.out.print("\nIngrese id: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					
					carrito = caja.agregarItem(carrito, id, cantidad);
					break;
					
				case 2:
					
					System.out.print("\nIngrese id: ");
					id = scanner.nextInt();
					caja.consultarItem(id);
					break;
					
				case 3:
					
					System.out.print("\nIngrese id: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					
					carrito = caja.eliminarItem(carrito, id, cantidad);
					break;
					
				case 4:
					
					caja.consultarCarrito(carrito);
					break;
				
				case 5:
					caja.finalizarVenta(carrito, idUsuario);
					carrito = new ArrayList<>();
					break;
					
				case 6:
					System.out.println("Saliendo...");
					scanner.close();
					System.exit(0);
			}
		}
	}

}
