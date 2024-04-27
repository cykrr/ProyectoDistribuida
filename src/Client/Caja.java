package Client;

import java.util.ArrayList;

import Common.Item;

public class Caja extends Cliente {
	
	public ArrayList<Item> agregarItem(ArrayList<Item> caja, int idProducto, int cantidad){
		//Cosas
		return caja;
	}
	
	public void consultarItem(int id){
		System.out.println("Nombre: yo k se");
		System.out.println("Precio: si");
	}
	
	public ArrayList<Item> eliminarItem(ArrayList<Item> caja, int idProducto, int cantidad){
		//Cosas
		return caja;
	}
	
	public void consultarCarrito(ArrayList<Item> caja) {
		int total = 0;
		for (Item elemento : caja) {
		    System.out.println(elemento.getNombre());
		    System.out.println(elemento.getCantidad());
		    System.out.println(elemento.precioTotal());
		    total += elemento.precioTotal();
		}
	}
	
	public void finalizarVenta() {
		//Guardar boleta o que se yo
		System.out.println("Gacias por comprar!");
	}
	
}
