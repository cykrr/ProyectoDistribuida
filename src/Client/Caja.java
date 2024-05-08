package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import Common.APIDownException;
import Common.Boleta;
import Common.Item;
import Common.ItemCarrito;
import Common.ProductNotFoundException;
import Common.Usuario;

public class Caja extends Cliente {
	protected Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public Caja() throws RemoteException, NotBoundException {
		super();
		this.usuario = new Usuario(-1, "nadie", -1);
	}
	
	public boolean logIn(int id, int clave) {
		try {
			System.out.println("Iniciando sesión");
			Usuario usuario = servidor.logIn(id, clave);
			if (usuario == null) {
				System.out.println("Credenciales incorrectas");
				return false;
			}
			this.usuario = usuario;
			return true;
		} catch (RemoteException e) {
			System.out.println("No se pudo iniciar sesión");
			e.printStackTrace();
			this.usuario = null;
			return false;
		}
	}
	
	public ArrayList<ItemCarrito> agregarItem(ArrayList<ItemCarrito> caja, int id, int cantidad) throws RemoteException, APIDownException, ProductNotFoundException{
		for (ItemCarrito elemento : caja) {
			if(elemento.getItem().getId() == id) {
				elemento.setCantidad(elemento.getCantidad()+cantidad);
				System.out.println("Agregado " + elemento.getItem().getNombre() + "x" + cantidad);
				return caja;
			}
		}
		
		Item item = servidor.obtenerItem(id);

		ItemCarrito itemCarrito = new ItemCarrito(item,cantidad);
		caja.add(itemCarrito);
		return caja;
	}
	
	public void consultarItem(int id) {
		try {
			Item item = servidor.obtenerItem(id);
			System.out.println("\nNombre:" + item.getNombre());
			System.out.println("Cantidad disponible: "+ servidor.obtenerStock(id));
			System.out.println("Precio base: "+ item.getPrecio());
			if(item.getDescuento() == 1) {
				System.out.println("Precio con descuento: "+ item.getPrecioDescuento());
			}
		} catch (ProductNotFoundException e){
			System.out.println("No se encontró el item con ID " + id );
		} catch (APIDownException e) {
			System.out.println("No se pudo establecer conexión con la API");
		} catch (RemoteException e) {
			System.out.println("No se pudo obtener el item con ID " + id);
		} catch (SQLException e) {
			System.out.println("No se pudo establecer conexión con la base de datos");
		}
	}
	
	public ArrayList<ItemCarrito> eliminarItem(ArrayList<ItemCarrito> caja, int idProducto, int cantidad){
		for (ItemCarrito elemento : caja) {
			if(elemento.getItem().getId() == idProducto) {
				elemento.setCantidad(elemento.getCantidad()-cantidad);
				if(elemento.getCantidad() <= 0) {
					caja.remove(elemento);
					System.out.println("Eliminado " + elemento.getItem().getNombre());
				}else {
					System.out.println("Eliminado " + elemento.getItem().getNombre() + "x" + cantidad);
				}
				return caja;
			}
		}
		System.out.println("\nEl producto no está en la caja");
		return caja;
	}
	
	public void consultarCarrito(ArrayList<ItemCarrito> caja) throws RemoteException {
		int total = 0;
		System.out.println("\n Resumen boleta:");
		for (ItemCarrito elemento : caja) {
		    System.out.println("-" + elemento.getItem().getNombre() + " x" + elemento.getCantidad() + " total:" + elemento.getCantidad()*elemento.getItem().getPrecioDescuento() + "$");
		    total += elemento.getCantidad()*elemento.getItem().getPrecioDescuento();
		}
		if(total == 0) {
			System.out.println("Carrito vacío");
		}else {
			System.out.println("Total a pagar: " + total + "$");
		}
	}
	
	public void finalizarVenta(ArrayList<ItemCarrito> caja, int idCajero) throws RemoteException, SQLException {
		if (this.usuario == null) {
			System.out.println("Inicie sesión para concretar venta");
			return;
		}

		int id = servidor.generarBoleta(caja, idCajero);
		Boleta boleta = servidor.obtenerBoleta(id);
		boleta.mostrar();
		System.out.println("Gracias por comprar!");
	}
	
}
