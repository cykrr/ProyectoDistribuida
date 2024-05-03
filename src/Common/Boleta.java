package Common;

import java.util.ArrayList;
import java.util.Iterator;

public class Boleta {
	private ArrayList<ItemBoleta> items;
	private String nombreCajero;
	private int idCajero;
	
	public Boleta(int idCajero, String nombreCajero) {
		items = new ArrayList<>();
		this.idCajero = idCajero;
		this.nombreCajero = nombreCajero;
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
	
	public String getNombreCajero() {
		return nombreCajero; 
	}
	
	public int getIdCajero() {
		return idCajero;
	}
	
}
