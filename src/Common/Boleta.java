package Common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Boleta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ItemBoleta> items;
	private String nombreCajero;
	
	public Boleta(String nombreCajero) {
		items = new ArrayList<>();
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
	
}
