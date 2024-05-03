package Common;

public class ItemCarrito {
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
