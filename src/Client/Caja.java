package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

import Common.Boleta;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemCarrito;
import Common.ProductNotFoundException;

public class Caja extends Cliente {

	public Caja() throws RemoteException, NotBoundException {
		super();
	}

	public ArrayList<ItemCarrito> agregarItem(ArrayList<ItemCarrito> caja, int id, int cantidad) throws RemoteException{
		
		for (ItemCarrito elemento : caja) {
			if(elemento.getItem().getId() == id) {
				elemento.setCantidad(elemento.getCantidad()+cantidad);
				return caja;
			}
		}
		ItemCarrito item = new ItemCarrito(servidor.obtenerItem(id),cantidad);
		caja.add(item);
		return caja;
	}
	
	public void consultarItem(int id) throws RemoteException{
		try {
			Item item = servidor.obtenerItem(id);
			System.out.println("\nNombre:" + item.getNombre());
			System.out.println("Precio base: "+ item.getPrecio());
			if(item.getDescuento() == 1) {
				System.out.println("Precio con descuento: "+ item.getPrecioDescuento());
			}
		}catch(ProductNotFoundException e){
			System.out.println("No se pudo obtener el item");
			throw e;
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
		    System.out.println("-" + elemento.getItem().getNombre() + " x" + elemento.getCantidad() + " total:" + elemento.getCantidad()*elemento.getItem().getPrecio() + "$");
		    total += elemento.getCantidad()*elemento.getItem().getPrecio();
		}
		if(total == 0) {
			System.out.println("Carrito vacío");
		}else {
			System.out.println("Total a pagar: " + total + "$");
		}
	}
	
	public void finalizarVenta(ArrayList<ItemCarrito> caja, int idCajero) throws RemoteException, SQLException {
		servidor.generarBoleta(caja, idCajero);
		System.out.println("Gacias por comprar!");
	}
	
}
