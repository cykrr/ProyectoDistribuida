package Common;

import java.util.ArrayList;
import java.util.Iterator;

public class Boleta implements java.io.Serializable {
	
	private ArrayList<ItemBoleta> items;
	private String nombreCajero;
	private int idCajero;
	private int id; 

	
	public Boleta(int idCajero, String nombreCajero) {
		items = new ArrayList<>();
		this.idCajero = idCajero;
		this.nombreCajero = nombreCajero;
	}

	public Boleta(int idCajero, String nombreCajero, int id) {
		items = new ArrayList<>();
		this.idCajero = idCajero;
		this.nombreCajero = nombreCajero;
		this.id = id;
	}
	
	public void agregarItem(ItemBoleta item) {
		items.add(item);
	}
	
	public Iterator<ItemBoleta> getItems() {
		return items.iterator();
	}
	
	public int calcularPrecioFinal() {
		int total = 0;
		for(int i = 0; i < items.size(); i++) {
			total += items.get(i).getPrecioTotal();
		}
		return total;
	}

	public void mostrar() {
		System.out.println("ID: " + getId());
		System.out.println("Total: " + calcularPrecioFinal());
		System.out.println("Cajero: " + getNombreCajero());
		System.out.println("Items:");
		Iterator <ItemBoleta> items = getItems();
		while (items.hasNext()) {
			ItemBoleta item = items.next();
			System.out.println("Nombre: " + item.getNombreProducto());
			System.out.println("Cantidad: " + item.getCantidad());
			System.out.println("Precio Neto: " + item.getPrecioTotal());
		}

	}
	
	public String getNombreCajero() {
		return nombreCajero; 
	}
	
	public int getIdCajero() {
		return idCajero;
	}

	public int getId() {
		return id;
	}
	
}
