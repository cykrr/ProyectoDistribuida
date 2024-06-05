package Client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import Common.APIDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.CajeroNotFoundException;
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
	
	public void agregarStock(int id, int cantidad) throws Exception {
		try {
			System.out.println("Iniciando solicitud para agregar stock...\n");
			servidor.agregarStock(id, cantidad);
			System.out.println("Stock actualizado con éxito\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		}
	}
	
	public void eliminarStock(int id, int cantidad) throws Exception  {
		try {
			System.out.println("Iniciando solicitud para eliminar stock...\n");
			servidor.eliminarStock(id, cantidad);
			System.out.println("Stock actualizado con éxito\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		}
	}
	
	public void consultarStock(int id) throws Exception {
		try {
			System.out.println("Iniciando solicitud para consultar stock...\n");
			int stock = servidor.obtenerStock(id);
			Item item = servidor.obtenerItem(id);
			
			System.out.println("Producto: " + item.getNombre());
			System.out.println("Stock: " + stock + "\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		}
	}
	
	public void consultarBoleta(int id) throws Exception {
		try {
			System.out.println("Iniciando solicitud para obtener boleta...\n");
			Boleta boleta = servidor.obtenerBoleta(id);
			System.out.println("\nDatos de boleta con ID " + id);
			Iterator<ItemBoleta> it = boleta.getItems();
			while(it.hasNext()) {
				ItemBoleta item = it.next();
				System.out.println("- " + item.getNombreProducto() + " x" + item.getCantidad() + " Total: $" + item.getPrecioTotal());
			}
			System.out.println("Total a pagar: $" + boleta.calcularPrecioFinal());
			System.out.println("Nombre cajero: " + boleta.getNombreCajero() + "\n");
		} catch (BoletaNotFoundException e) {
			System.out.println("No se encontró la boleta con ID " + id + "\n");
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		}
	}
	
	public void mostrarCajeros() throws Exception {
		System.out.println("Iniciando solicitud para obtener cajeros...\n");
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
	}
	
	public void agregarCajero(String nombre, int clave) throws Exception {
		System.out.println("Iniciando solicitud para agregar cajero...\n");
		servidor.agregarCajero(nombre, clave);
		System.out.println(String.format("Cajero agregado con éxito\n"));
	}
	
	public void eliminarCajero(int id) throws Exception {
		try {
			System.out.println("Iniciando solicitud para eliminar cajero...\n");
			servidor.eliminarCajero(id);
			System.out.println(String.format("Cajero con ID %d eliminado con éxito\n", id));
		} catch (CajeroNotFoundException e) {
			System.out.println("No se encontró el cajero con ID " + id + "\n");
		}
	}

	public void mostrarMenu() throws Exception {
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
