package Common;

public class Item {
	
	private int id = 0;
	private int cantidad = 0;
	private int precio = 0;
	private String nombre = "Nombre";
	
	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}
	
	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public int getCantidad() {
	    return cantidad;
	}

	public void setCantidad(int cantidad) {
	    this.cantidad = cantidad;
	}

	public String getNombre() {
	    return nombre;
	}

	public void setNombre(String nombre) {
	    this.nombre = nombre;
	}
	
	public int precioTotal() {
		return cantidad*precio;
	}
	
}
