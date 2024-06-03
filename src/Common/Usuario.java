package Common;

import java.io.Serializable;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private int clave;
    private int rol;

    public Usuario(int id, String nombre, int clave, int rol) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getClave() {
    	return clave;
    }

    public int getRol() {
        return rol;
    }
}
