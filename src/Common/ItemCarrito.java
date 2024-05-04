package Common;

import java.io.Serializable;

public class ItemCarrito implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4376263176805318571L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private Item item;
	private int cantidad;
	
	public ItemCarrito(Item item, int cantidad) {
		this.item = item;
		this.cantidad = cantidad;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
