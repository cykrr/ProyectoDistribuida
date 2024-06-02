package Client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



import Common.APIDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemBoleta;
import Common.ProductNotFoundException;
import Common.Usuario;
import Common.Colors;


public class Administrador {
	private Usuario usuario;
	private Scanner scanner;
	private InterfazServidor servidor;

	public Administrador(Usuario usuario, InterfazServidor servidor, Scanner scanner) {
		this.usuario = usuario;
		this.scanner = scanner;
		this.servidor = servidor;
	}


	private void threadRun(Callable<Void> callable) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> future = executor.submit(() -> {
				try {
					callable.call();
				} catch (Exception e) {
					System.out.println("[ThreadRun]" + e.getMessage());
				}
		});
		try {
			if (!future.isDone()) {
				future.get(1, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e ) {
			System.out.println("Espere por favor..\n");
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Ocurrió un error con el servidor\n");
		} finally {
			executor.shutdownNow();
		}

	}
	
	public void agregarStock(int id, int cantidad)  {
		try {
			threadRun( () -> {
			servidor.acquireMutex();
			servidor.agregarStock(id, cantidad);
			servidor.releaseMutex();
			return null;
			});
			System.out.println("Stock actualizado con éxito\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (RuntimeException e) {
			System.out.println(Colors.ANSI_RED + e.getMessage() + Colors.ANSI_RESET);
		} catch (Exception e) {
			System.out.println("Ocurrió un error con el servidor\n");
		}
	}
	
	public void eliminarStock(int id, int cantidad)  {
		try {
			threadRun(() -> {
				servidor.acquireMutex();
				servidor.eliminarStock(id, cantidad);
				servidor.releaseMutex();
				return null;
			});
			System.out.println("Stock actualizado con éxito\n");
		} catch (RemoteException e) {
			System.out.println("Ocurrió un error en la conexión con el servidor\n");
		} catch (ProductNotFoundException e) {
			System.out.println("No se encontró el item con ID " + id + "\n");
		} catch (RuntimeException e) {
			System.out.println(Colors.ANSI_RED + e.getMessage() + Colors.ANSI_RESET);
		} catch (Exception e) {
			System.out.println("Ocurrió un error con el servidor\n");
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
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		} catch (RuntimeException e) {
			System.out.println(Colors.ANSI_RED + e.getMessage() + Colors.ANSI_RESET);
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
		} catch (BoletaNotFoundException e) {
			System.out.println("No se encontró la boleta con ID " + id + "\n");
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API\n");
		} catch (RuntimeException e) {
			System.out.println(Colors.ANSI_RED + e.getMessage() + Colors.ANSI_RESET);
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
