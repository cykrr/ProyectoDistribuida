package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Common.Boleta;
import Common.InterfazServidor;
import Common.Item;

public class Caja extends Cliente {

	public Caja() throws RemoteException, NotBoundException {
		super();
	}

	public ArrayList<Item> agregarItem(ArrayList<Item> caja, int idProducto, int cantidad) throws RemoteException{
		
		for (Item elemento : caja) {
			if(elemento.getId() == idProducto) {
				elemento.setCantidad(elemento.getCantidad()+cantidad);
				return caja;
			}
		}
		Item item = new Item();
		item.setId(idProducto);
		item.setCantidad(cantidad);
		item.setPrecio(servidor.obtenerPrecio(idProducto));
		caja.add(item);
		return caja;
	}
	
	public void consultarItem(int id) throws RemoteException{
		int precio = servidor.obtenerPrecio(id);
		System.out.println("\nID:" + id);
		System.out.println("Precio: "+ precio);
	}
	
	public ArrayList<Item> eliminarItem(ArrayList<Item> caja, int idProducto, int cantidad){
		for (Item elemento : caja) {
			if(elemento.getId() == idProducto) {
				elemento.setCantidad(elemento.getCantidad()-cantidad);
				if(elemento.getCantidad() <= 0) {
					caja.remove(elemento);
				}
				return caja;
			}
		}
		System.out.println("\nEl producto no está en la caja");
		return caja;
	}
	
	public void consultarCarrito(ArrayList<Item> caja) throws RemoteException {
		int total = 0;
		System.out.println("\n Resumen boleta:");
		for (Item elemento : caja) {
		    System.out.println("-ID: " + elemento.getId() + " x" + elemento.getCantidad() + " total:" + elemento.precioTotal() + "$");
		    total += elemento.precioTotal();
		}
		System.out.println("Total a pagar: " + total + "$");
	}
	
	public void finalizarVenta() throws RemoteException {
		//Guardar boleta o que se yo
		Boleta boleta = new Boleta();
		servidor.enviarBoleta(boleta);
		System.out.println("Gacias por comprar!");
	}
	
}
