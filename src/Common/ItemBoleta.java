package Common;

import java.io.Serializable;

public class ItemBoleta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idProducto;
	private String nombreProducto;
	private int precioTotal; // Precio final después de aplicar descuentos/promociones
	private int cantidad;
	
	public ItemBoleta(int idProducto, String nombreProducto, int precioTotal, int cantidad) {
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.precioTotal = precioTotal;
		this.cantidad = cantidad;
	}
	
	public int getIdProducto() {
		return idProducto;
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	public int getPrecioTotal() {
		return precioTotal;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
}
