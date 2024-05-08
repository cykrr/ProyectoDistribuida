package Common;

import java.io.Serializable;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private int rol;

    public Usuario(int id, String nombre, int rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRol() {
        return rol;
    }
}
