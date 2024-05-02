package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.Item;


public class RunCaja {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		@SuppressWarnings("unused")
		Cliente cliente = new Cliente();
		//cliente.startClient();
		Caja caja = new Caja();
		ArrayList<Item> carrito = new ArrayList<>();
		
		////////Variables para leer porque no sé hacerlo de forma más eficiente/////
		
		int id = 0;
		int cantidad = 0;
		
		//////////////////////////////////////////////////////////////////////////
		
		Scanner scanner = new Scanner(System.in);
		
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
					
				case 2:
					
					System.out.print("\nIngrese id: ");
					id = scanner.nextInt();
					caja.consultarItem(id);
					
				case 3:
					
					System.out.print("\nIngrese id: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					
					carrito = caja.eliminarItem(carrito, id, cantidad);
					
				case 4:
					
					caja.consultarCarrito(carrito);
				
				case 5:
					caja.finalizarVenta();
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
