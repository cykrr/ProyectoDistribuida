package Client;

import java.util.Scanner;

import Common.InterfazServidor;
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
		
	}
	
	public void eliminarStock(int id, int cantidad) {
		
	}
	
	public void consultarStock(int id) {
		
	}
	
	public void consultarBoleta(int id) {
		
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
			System.out.println("9. Cerrar sesión");
			System.out.println("0. Salir\n");
			System.out.print("Opción: ");
			
			opcion = scanner.nextInt();
			System.out.println();
			
			int id, cantidad;
			
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
