package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Common.Boleta;
import Common.InterfazServidor;
import Common.Item;
import Common.ProductNotFoundException;

public class Caja extends Cliente {

	public Caja() throws RemoteException, NotBoundException {
		super();
	}

	public ArrayList<Item> agregarItem(ArrayList<Item> caja, int id, int cantidad) throws RemoteException{
		
		for (Item elemento : caja) {
			if(elemento.getId() == id) {
				elemento.setCantidadPack(elemento.getCantidadPack()+cantidad);
				return caja;
			}
		}
		Item item = servidor.obtenerItem(id);
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
	
	public ArrayList<Item> eliminarItem(ArrayList<Item> caja, int idProducto, int cantidad){
		for (Item elemento : caja) {
			if(elemento.getId() == idProducto) {
				elemento.setCantidadPack(elemento.getCantidadPack()-cantidad);
				if(elemento.getCantidadPack() <= 0) {
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
		    System.out.println("-ID: " + elemento.getId() + " x" + elemento.getCantidadPack() + " total:" + elemento.getCantidadPack()*elemento.getPrecio() + "$");
		    total += elemento.getCantidadPack()*elemento.getPrecio();
		}
		System.out.println("Total a pagar: " + total + "$");
	}
	
	public void finalizarVenta() throws RemoteException {
		//Guardar boleta o que se yo
		//Boleta boleta = new Boleta();
		//servidor.enviarBoleta(boleta);
		System.out.println("Gacias por comprar!");
	}
	
}
