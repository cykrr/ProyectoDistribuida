package Common;

public class Item implements java.io.Serializable {

	/**
	 * 
	 */
	// Process JSON with json Jackson and extract json.comertialOffer.{discountValue, packPrice, price, priceWithoutDiscount, quantity} 

	private static final long serialVersionUID = 1L;
	
	private int id = 0;
	private int cantidad = 0;
	private int precio = 0;
	private int precioPack = 0;
	private int precioDescuento = 0;
	private int descuento = 0;
	private String nombre = "N/A";

	public Item(int id, int cantidad, int precio, int precioPack, int precioDescuento, int descuento, String nombre) {
		this.id = id;
		this.cantidad = cantidad;
		this.precio = precio;
		this.precioPack = precioPack;
		this.precioDescuento = precioDescuento;
		this.descuento = descuento;
		this.nombre = nombre;
	}

	public int getPrecioDescuento() {
		return precioDescuento;
	}

	public int getDescuento() {
		return descuento;
	}

	public int getPrecioPack() {
		return precioPack;
	}
	
	public int getId() {
	    return id;
	}

	public int getCantidad() {
	    return cantidad;
	}

	public String getNombre() {
	    return nombre;
	}

	
	public int precioTotal() {
		return cantidad*precio;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", cantidad=" + cantidad + ", precio=" + precio + ", precioPack=" + precioPack
				+ ", precioDescuento=" + precioDescuento + ", descuento=" + descuento + ", nombre=" + nombre + "]";
	}
	
}
