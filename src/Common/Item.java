package Common;

public class Item implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id = 0;
	private int precio = 0;
	private int descuento = 0;
	private int precioDescuento = 0;
	private int cantidadPack = 0;
	private int precioPack = 0;
	private String nombre = "N/A";

	public Item(int id, int precio, int descuento, int precioDescuento, int cantidadPack, int precioPack, String nombre) {
		this.id = id;
		this.precio = precio;
		this.descuento = descuento;
		this.precioDescuento = precioDescuento;
		this.cantidadPack = cantidadPack;
		this.precioPack = precioPack;
		this.nombre = nombre;
	}
	
	public int getId() {
	    return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPrecio() {
		return precio;
	}
	
	public int getDescuento() {
		return descuento;
	}

	public int getPrecioDescuento() {
		return precioDescuento;
	}
	
	public int getCantidadPack() {
	    return cantidadPack;
	}

	public int getPrecioPack() {
		return precioPack;
	}

	public String getNombre() {
	    return nombre;
	}
	
	public void setCantidadPack(int cantidad) {
		this.cantidadPack = cantidad;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", cantidadPack=" + cantidadPack + ", precio=" + precio + ", precioPack=" + precioPack
				+ ", precioDescuento=" + precioDescuento + ", descuento=" + descuento + ", nombre=" + nombre + "]";
	}
	
}
