package Client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Common.APIDownException;
import Common.Boleta;
import Common.Item;
import Common.ItemCarrito;
import Common.ProductNotFoundException;


@SuppressWarnings("unused")
public class RunCaja {
	public boolean finalizarVenta;

	public static void main(String[] args) {
		
		Caja caja = null;
		try {
        	caja = new Caja();
        }catch(RemoteException e) {
        	System.out.println("No se encontró el servidor");
			return;
        } catch (NotBoundException e) {
			System.out.println("No se encontró el servidor");
			return;
		}
		
        boolean esAdmin = false;
        
		ArrayList<ItemCarrito> carrito = new ArrayList<>();
		
		////////Variables para leer/////
		
		int id = 0;
		int cantidad = 0;
		int clave = 0;
		
		/////////////////////////////////
		
		Scanner scanner = new Scanner(System.in);


		outerloop: while (true) {
			if(caja.usuario.getId() < 0) {
				System.out.println("----No se ha iniciado sesión----");
				System.out.println("6. Iniciar sesión");
			} else {
				System.out.println("---Sesión iniciada con id: " + caja.usuario.getNombre() + "----");
				System.out.println("\nPor favor, elige una opción:");
				System.out.println("1. Agregar producto al carrito");
				System.out.println("2. Consultar producto");
				System.out.println("3. Eliminar producto del carrito");
				System.out.println("4. Consultar carrito");
				System.out.println("5. Finalizar venta");
				System.out.println("6. Obtener boleta");
				System.out.println("9. Cerrar sesión");
			}
			
			System.out.println("0. Salir");
			System.out.print("Opción: ");
			
			
			int opcion = scanner.nextInt();
			if(caja.usuario.getId() < 0 && opcion!=6 && opcion!=0) {
				continue outerloop;
			}
			
			switch (opcion) {
			
				case 1:
					if (caja.usuario.getId() < 0) {
						System.out.println("Inicie sesión para consultar productos");
						break;
					}

					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					
					System.out.print("Ingrese cantidad: ");
					cantidad = scanner.nextInt();
					while(cantidad <=0) {
						System.out.print("Ingrese valores mayores a 0 \nIntente nuevamente: ");
						cantidad = scanner.nextInt();
					}
					try {
						caja.agregarItem(carrito, id, cantidad);

					} catch (APIDownException e) {
						System.out.println("No se pudo obtener el precio del producto");
					} catch (RemoteException e) {
						System.out.println("No se pudo contactar el Servidor");
					} catch (Exception e ) {
						System.err.println("Ocurrió un error. Intente nuevamente");
					}
					break;
					
				case 2:
					if (caja.usuario == null) {
						System.out.println("Inicie sesión para continuar");
						break;
					}
					
					System.out.print("\nIngrese id del producto: ");
					id = scanner.nextInt();
					try {
						caja.consultarItem(id);

					} catch (ProductNotFoundException e) {
						e.printStackTrace();
					}

					break;
					
				case 3:
					if (caja.usuario == null) {
						System.out.println("Inicie sesión para continuar");
						break;
					}
					
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
					if (caja.usuario == null) {
						System.out.println("Inicie sesión para continuar");
						break;
					}
					
					try {
						caja.consultarCarrito(carrito);
					} catch (RemoteException e) {
						System.out.println("No se pudo consultar el carrito");
						e.printStackTrace();
					}
					break;
				
				case 5:
					if (caja.usuario == null) {
						System.out.println("Inicie sesión para consultar productos");
						break;
					}
						if (caja.usuario != null) {
							try {
								caja.finalizarVenta(carrito, caja.usuario.getId());
							} catch (RemoteException e) {
								System.out.println("No se pudo contactar al servidor");
								e.printStackTrace();
							} catch (SQLException e) {
								System.out.println("No se pudo finalizar la venta");
								e.printStackTrace();
							}
						} else {
							System.out.println("Inicie sesión para concretar venta");
						}
					break;
				
				case 6:
					System.out.println("Inicio de sesión");
					System.out.println("Ingrese id:");
					id = scanner.nextInt();
					
					System.out.print("Ingrese clave: ");
					clave = scanner.nextInt();
					caja.logIn(id,clave);
					break;
				case 7:
					System.out.println("Ingrese id de la boleta:");
					id = scanner.nextInt();
					try {
						Boleta boleta = caja.servidor.obtenerBoleta(id);
						if (boleta != null) {
							System.out.println("Boleta encontrada");
							boleta.mostrar();
						} else {
							System.out.println("Boleta no encontrada");
						}

					} catch (RemoteException e) {
						System.out.println("No se pudo obtener la boleta");
					} catch (SQLException e) {
						System.out.println("No se pudo obtener la boleta");

					}

					
					break;
					
				case 9:
					if (caja.usuario == null) {
						System.out.println("Inicie sesión para cerrar sesión");
						break;
					}
					caja.usuario = null;
					break;
					
					
				case 0:
					System.out.println("Saliendo...");
					scanner.close();
					System.exit(0);
			}
		}
	}

}
