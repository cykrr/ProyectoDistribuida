package Client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.Item;
import Common.ItemCarrito;


@SuppressWarnings("unused")
public class RunCaja {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
		
		Caja caja = null;
		try {
        	caja = new Caja();
        }catch(RemoteException e) {
        	System.out.print("No se encontró el servidor");
        }
		
        int idUsuario = -1;
        boolean esAdmin = false;
        
		ArrayList<ItemCarrito> carrito = new ArrayList<>();
		
		////////Variables para leer/////
		
		int id = 0;
		int cantidad = 0;
		int clave = 0;
		
		/////////////////////////////////
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Inicie Sesión para continuar...\n");
		while(true) {
			
			System.out.println("Ingrese id:");
			id = scanner.nextInt();
			
			System.out.print("Ingrese clave: ");
			clave = scanner.nextInt();
			
			if(caja.logIn(id,clave) == true) {
				esAdmin = false;
				if(esAdmin == true) {
					idUsuario = id;
					Administrador administrador = new Administrador();
					administrador.menu(idUsuario);
					
				}else {
					idUsuario=id;
					break;
					//System.out.println("Error al iniciar sesión...\nIntente nuevamente.\n");
				}
			}else {
				System.out.println("Usuario no encontrado.\n\nVerifique que los datos sean correctos.\nIntente de nuevo.");
			}
			
		}
		
		
		
		while (idUsuario != -1) {
			
			System.out.println("---Sesión iniciada con id: " + idUsuario + "----");
			
			System.out.println("\nPor favor, elige una opción:");
			
			System.out.println("1. Agregar producto al carrito");
			System.out.println("2. Consultar producto");
			System.out.println("3. Eliminar producto del carrito");
			System.out.println("4. Consultar carrito");
			System.out.println("5. Finalizar venta");
			System.out.println("9. Cerrar sesión"); //Esta opción no estaria valida, ya que o se cierra el programa o se queda en blanco
			System.out.println("0. Salir");
			System.out.print("Opción: ");
			
			
			int opcion = scanner.nextInt();
			if(opcion < 0) {
				System.out.println("Recuerde ingresar valores válidos");
			}
			
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
					
					carrito = caja.agregarItem(carrito, id, cantidad);
					break;
					
				case 2:
					
					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					caja.consultarItem(id);
					break;
					
				case 3:
					
					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad a eliminar: ");
					cantidad = scanner.nextInt();
					while(cantidad <=0) {
						System.out.print("Ingrese valores mayores a 0 \nIntente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					
					
					carrito = caja.eliminarItem(carrito, id, cantidad);
					break;
					
				case 4:
					
					caja.consultarCarrito(carrito);
					break;
				
				case 5:
					if(idUsuario != -1) {
						caja.finalizarVenta(carrito, idUsuario);
						carrito = new ArrayList<>();
					}else {
						System.out.println("Inicie sesión para concretar venta");
					}
					break;
					
				case 9:
					
					if(carrito.size() == 0) {
						idUsuario = -1;
						System.out.print("Sesión cerrada\n");
					}else {
						System.out.print("No puede cerrar sesión con una venta en proceso");
					}
					break;
					
					
				case 0:
					System.out.println("Saliendo...");
					scanner.close();
					System.exit(0);
			}
		}
	}

}
