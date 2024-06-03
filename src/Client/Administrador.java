package Client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import Common.APIDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemBoleta;
import Common.ProductNotFoundException;
import Common.Usuario;

public class Administrador {
	private Usuario usuario;
	private Scanner scanner;
	private InterfazServidor servidor;

	public Administrador(Usuario usuario, InterfazServidor servidor, Scanner scanner) {
		this.usuario = usuario;
		this.scanner = scanner;
		this.servidor = servidor;
	}
	
	public void agregarStock(int id, int cantidad) {
		try {
			servidor.agregarStock(id, cantidad);
			System.out.println("Stock actualizado con éxito\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (SQLException e) {
			System.out.println("No se pudo establecer conexión con la base de datos\n");
		}
	}
	
	public void eliminarStock(int id, int cantidad) {
		try {
			servidor.eliminarStock(id, cantidad);
			System.out.println("Stock actualizado con éxito\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (SQLException e) {
			System.out.println("No se pudo establecer conexión con la base de datos\n");
		}
	}
	
	public void consultarStock(int id) {
		try {
			int stock = servidor.obtenerStock(id);
			Item item = servidor.obtenerItem(id);
			
			System.out.println("Producto: " + item.getNombre());
			System.out.println("Stock: " + stock + "\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (SQLException e) {
			System.out.println("No se pudo establecer conexión con la base de datos\n");
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		}
	}
	
	public void consultarBoleta(int id) {
		try {
			Boleta boleta = servidor.obtenerBoleta(id);
			System.out.println("\nDatos de boleta con ID " + id);
			Iterator<ItemBoleta> it = boleta.getItems();
			while(it.hasNext()) {
				ItemBoleta item = it.next();
				System.out.println("- " + item.getNombreProducto() + " x" + item.getCantidad() + " Total: $" + item.getPrecioTotal());
			}
			System.out.println("Total a pagar: $" + boleta.calcularPrecioFinal());
			System.out.println("Nombre cajero: " + boleta.getNombreCajero() + "\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("No se pudo establecer conexión con la base de datos\n");
		} catch (BoletaNotFoundException e) {
			System.out.println("No se encontró la boleta con ID " + id + "\n");
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		}
	}
	
	public void mostrarCajeros() {
		try {
			ArrayList<Usuario> cajeros = servidor.obtenerCajeros();
			if (cajeros.size() == 0) {
				System.out.println("No hay cajeros registrados\n");
				return;
			}
			
			System.out.println(String.format("Mostrando datos para %d cajeros registrados\n", cajeros.size()));
			System.out.println(String.format("%-10s %-20s %s", "ID", "Nombre", "Clave"));
			for(int i = 0; i < cajeros.size(); i++) {
				Usuario cajero = cajeros.get(i);
				System.out.println(String.format("%-10d %-20s %d", cajero.getId(), cajero.getNombre(), cajero.getClave()));
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n");
		}
	}
	
	public void agregarCajero(String nombre, int clave) {
		try {
			servidor.agregarCajero(nombre, clave);
			System.out.println(String.format("Cajero agregado con éxito\n"));
		} catch(Exception e) {
			System.out.println(e.getMessage() + "\n");
		}
	}
	
	public void eliminarCajero(int id) {
		try {
			servidor.eliminarCajero(id);
			System.out.println(String.format("Cajero con ID %d eliminado con éxito\n", id));
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n");
		}
	}

	public void mostrarMenu() {
		int opcion;
		
		do {
			System.out.println("¡Bienvenido " + usuario.getNombre() + "! Tu id: " + usuario.getId() + "\n");
			System.out.println("Por favor, elige una opción:\n");
			System.out.println("1. Añadir stock a producto");
			System.out.println("2. Eliminar stock a producto");
			System.out.println("3. Consultar stock");
			System.out.println("4. Consultar boleta");
			System.out.println("5. Mostrar cajeros");
			System.out.println("6. Agregar cajero");
			System.out.println("7. Eliminar cajero");
			System.out.println("9. Cerrar sesión");
			System.out.println("0. Salir\n");
			System.out.print("Opción: ");
			
			opcion = scanner.nextInt();
			System.out.println();
			
			int id, cantidad, clave;
			String nombre;
			
			switch (opcion) {
				case 1:
					System.out.print("Ingrese id del producto: ");
					id = scanner.nextInt();
				
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					
					while(cantidad <= 0) {
						System.out.println("Ingrese valores mayores a 0");
						System.out.println("Intente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					
					agregarStock(id, cantidad);
					break;
					
				case 2:
					System.out.print("Ingrese id del producto: ");
					id = scanner.nextInt();
				
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					
					while(cantidad <= 0) {
						System.out.println("Ingrese valores mayores a 0");
						System.out.println("Intente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					
					eliminarStock(id, cantidad);
					break;
					
				case 3:
					System.out.print("Ingrese id del producto: ");
					id = scanner.nextInt();
					
					consultarStock(id);
					break;
					
				case 4:
					System.out.print("Ingrese id de la boleta: ");
					id = scanner.nextInt();
					
					consultarBoleta(id);
					break;
					
				case 5:
					mostrarCajeros();
					break;
					
				case 6:
					System.out.print("Ingrese nombre del cajero: ");
					scanner.nextLine();
					nombre = scanner.nextLine();
					
					System.out.print("Ingrese clave numérica del cajero: ");
					clave = scanner.nextInt();
					
					agregarCajero(nombre, clave);
					break;
				
				case 7:
					System.out.print("Ingrese id del cajero: ");
					id = scanner.nextInt();
					
					eliminarCajero(id);
					break;
					
				case 9:
					System.out.println("Cerrando sesión...\n");
					break;
							
				case 0:
					System.out.println("Saliendo...");
					scanner.close();
					System.exit(0);
					
				default:
					System.out.println("Opción inválida\n");
			}
					
		} while(opcion != 9);
	}

}
