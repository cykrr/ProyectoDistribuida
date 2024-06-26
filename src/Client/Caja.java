package Client;

import java.util.ArrayList;
import java.util.Scanner;

import Common.APIDownException;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemCarrito;
import Common.ProductNotFoundException;
import Common.Usuario;

public class Caja {
	private Usuario usuario;
	private Scanner scanner;
	private InterfazServidor servidor;
	private ArrayList<ItemCarrito> carrito;

	public Caja(Usuario usuario, InterfazServidor servidor, Scanner scanner) {
		this.usuario = usuario;
		this.scanner = scanner;
		this.servidor = servidor;
		this.carrito = new ArrayList<>();
	}
	
	public void agregarItem(int id, int cantidad) throws Exception {
		for (ItemCarrito elemento : carrito) {
			if(elemento.getItem().getId() == id) {
				elemento.setCantidad(elemento.getCantidad() + cantidad);
				System.out.println("Se agregó " + cantidad + " x " + elemento.getItem().getNombre() + "\n");
				return;
			}
		}
		
		try {
			Item item = servidor.obtenerItem(id);	
			ItemCarrito itemCarrito = new ItemCarrito(item, cantidad);
			carrito.add(itemCarrito);
			System.out.println("Se agregaron " + cantidad + " x " + item.getNombre() + "\n");
			
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} 
	}
	
	public void consultarItem(int id) throws Exception {
		try {
			Item item = servidor.obtenerItem(id);
			System.out.println("Nombre: " + item.getNombre());
			System.out.println("Stock: " + servidor.obtenerStock(id));
			System.out.println("Precio base: " + item.getPrecio());
			System.out.println("Descuento: " + item.getDescuento() + "%");
			System.out.println("Precio descuento: " + item.getPrecioDescuento());
			if(item.getCantidadPack() > 1) {
				System.out.println("Promoción: " + item.getCantidadPack() + " x $" + item.getPrecioPack());
			}
			System.out.println();
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		} 
	}
	
	public void eliminarItem(int idProducto, int cantidad){
		for (ItemCarrito elemento : carrito) {
			if(elemento.getItem().getId() == idProducto) {
				elemento.setCantidad(elemento.getCantidad() - cantidad);
				if(elemento.getCantidad() <= 0) {
					carrito.remove(elemento);
					System.out.println("Se eliminó el producto " + elemento.getItem().getNombre() + "\n");
				} else {
					System.out.println("Se eliminó " + cantidad + " x " + elemento.getItem().getNombre() + "\n");
				}
				return;
			}
		}
		System.out.println("El producto no está en la caja\n");
	}
	
	public void consultarCarrito() {
		int total = 0;
		System.out.println("Resumen boleta:");
		for (ItemCarrito elemento : carrito) {
		    System.out.println("- " + elemento.getItem().getNombre() + " x" + elemento.getCantidad() + " Total: $" + elemento.getPrecioFinal());
		    total += elemento.getPrecioFinal();
		}
		if (total == 0) {
			System.out.println("Carrito vacío\n");
		} else {
			System.out.println("Total a pagar: $" + total + "\n");
		}
	}
	
	public void finalizarVenta() throws Exception {
		if (carrito.size() == 0) {
			System.out.println("El carrito no puede quedar vacío!");
			System.out.println("Por favor, agrega algunos productos.\n");
			return;
		}
		
		int idBoleta = servidor.generarBoleta(carrito, usuario.getNombre());
		carrito = new ArrayList<>();
		System.out.println("Boleta con ID " + idBoleta + " generada con éxito");
		System.out.println("¡Gracias por comprar!\n");
	}

	public void mostrarMenu() throws Exception {
		int opcion;
		
		do {
			System.out.println("¡Bienvenido " + usuario.getNombre() + "! Tu id: " + usuario.getId() + "\n");
			System.out.println("Por favor, elige una opción:\n");
			System.out.println("1. Agregar producto al carrito");
			System.out.println("2. Consultar producto");
			System.out.println("3. Eliminar producto del carrito");
			System.out.println("4. Consultar carrito");
			System.out.println("5. Finalizar venta");
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
					
					agregarItem(id, cantidad);
					break;
				
				case 2:
					System.out.print("Ingrese id del producto: ");
					id = scanner.nextInt();
					
					consultarItem(id);
					break;
				
				case 3:		
					System.out.print("Ingrese id del producto: ");
					id = scanner.nextInt();
				
					System.out.print("Ingrese cantidad a eliminar: ");
					cantidad = scanner.nextInt();
					
					while(cantidad <= 0) {
						System.out.println("Ingrese valores mayores a 0");
						System.out.println("Intente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					
					eliminarItem(id, cantidad);
					break;
				
				case 4:
					consultarCarrito();
					break;
					
				case 5:
					finalizarVenta();
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
