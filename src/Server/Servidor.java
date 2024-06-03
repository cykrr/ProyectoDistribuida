package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// import JSON Jackson core
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Common.APIDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.CajeroNotFoundException;
import Common.InterfazServidor;
import Common.Item;
import Common.ItemBoleta;
import Common.ItemCarrito;
import Common.ProductNotFoundException;
import Common.Rol;
import Common.StockMismatchException;
import Common.Usuario;

public class Servidor implements InterfazServidor {
	private static String apiUrlString = "http://localhost:5000";
	private Connection conn;

	public Servidor() throws IOException {
		UnicastRemoteObject.exportObject(this, 0);
		
		try {
			conn = createConnection();
			crearBD(conn);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return;
		}
		
		// test connection to API
		try {
			obtenerItem(1);
		} catch (APIDownException e) {
			System.err.println("API is down. Server will not be able to function properly.");
		}
	}
	
	private void crearBD(Connection conn) throws Exception {
		String queries;
		
		String path = "src/Server/db.sql";
		try {
			queries = readFile(path);
		} catch (IOException e) {
			System.out.println("Error al leer archivo " + path + ".\nNo se pudo crear la base de datos.");
			throw e;
		}
		
		String[] queriesArray = queries.split(";");
		
        for (int i = 0; i < queriesArray.length; i++) {
            queriesArray[i] = queriesArray[i].trim();
        }
		
        for (String query : queriesArray) {
        	try {
				Statement statement = conn.createStatement();
				statement.executeQuery(query);
			} catch (SQLException e) {
				System.out.println("Error al ejecutar sentencia " + query);
				throw e;
			}
        }
		
	}
	
	public Boleta obtenerBoleta(int idBoleta) throws RemoteException, BoletaNotFoundException, SQLException, APIDownException {
		String query = "SELECT nombreCajero, itemsBoleta.idProducto, itemsBoleta.precioTotal, itemsBoleta.cantidad "
				+ "FROM itemsBoleta JOIN boletas USING(idBoleta) WHERE idBoleta = %d";
		query = String.format(query, idBoleta);
		
		Statement statement = conn.createStatement();
		ResultSet data = statement.executeQuery(query);
		
		if (!data.next()) {
			throw new BoletaNotFoundException(idBoleta);
		}
		
		String nombreCajero = data.getString("nombreCajero");
		Boleta boleta = new Boleta(nombreCajero);
		
		do {
			int idProducto = data.getInt("idProducto");
			int precioTotal = data.getInt("precioTotal");
			int cantidad = data.getInt("cantidad");
			
			Item item = obtenerItem(idProducto);
			String nombreProducto = item.getNombre();
			
			ItemBoleta itemBoleta = new ItemBoleta(idProducto, nombreProducto, precioTotal, cantidad);
			boleta.agregarItem(itemBoleta);
		} while(data.next());
		
		return boleta;
	}
	
	public int generarBoleta(ArrayList<ItemCarrito> itemsCarrito, String nombreCajero) throws RemoteException, SQLException {
		try {
			// Empezar transacción
			conn.setAutoCommit(false);
			
			// Crear idBoleta y asignar a idCajero
			String query = "INSERT INTO boletas(nombreCajero) VALUES ('%s')";
			query = String.format(query, nombreCajero);
			
			PreparedStatement pstatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstatement.executeUpdate();

			ResultSet data = pstatement.getGeneratedKeys();
			data.next();
			int idBoleta = data.getInt(1);
			
			// Insertar items a la boleta
			Statement statement = conn.createStatement();
			String queryItem = "INSERT INTO itemsBoleta(idBoleta, idProducto, precioTotal, cantidad) VALUES (%d, %d, %d, %d)";
			String queryStock = "UPDATE stock SET stock = stock - %d WHERE idProducto = %d";
			String currentQuery;
			
			for(int i = 0; i < itemsCarrito.size(); i++) {
				ItemCarrito itemCarrito = itemsCarrito.get(i);
				Item item = itemCarrito.getItem();
				int stock = obtenerStock(item.getId());
				if (itemCarrito.getCantidad() > stock) {
					throw new StockMismatchException(item.getId(), stock);
				}

				int cantidad = itemCarrito.getCantidad();		
				int precioTotal = itemCarrito.getPrecioFinal();
				
				currentQuery = String.format(queryItem, idBoleta, item.getId(), precioTotal, cantidad);
				statement.executeQuery(currentQuery);
				
				// Actualizar stock
				currentQuery = String.format(queryStock, cantidad, item.getId());
				statement.executeUpdate(currentQuery);
			}
			
			// Finalizar transacción
			conn.commit();
			return idBoleta;
		}
		catch(Exception e) {
			conn.rollback(); // Si algo falla, descartar cambios.
			System.err.println(e);
			throw new SQLException("Error al generar boleta. Se ha hecho rollback.");
		}
		finally {
			conn.setAutoCommit(true);
		}
	}
	
	public int obtenerStock(int id) throws SQLException, ProductNotFoundException {
		Statement statement = conn.createStatement();
		String query = "SELECT stock FROM stock WHERE idProducto = %d";
		query = String.format(query,  id);
		
		ResultSet data = statement.executeQuery(query);
		if (!data.next()) {
			throw new ProductNotFoundException(id);
		}
		return data.getInt("stock");
	}
	
	public void agregarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException {
		Statement statement = conn.createStatement();
		String query = "UPDATE stock SET stock = stock + %d WHERE idProducto = %d";
		query = String.format(query, cantidad, id);
		
		int rowCount = statement.executeUpdate(query);
		if (rowCount == 0) {
			throw new ProductNotFoundException(id);
		}
	}
	
	public void eliminarStock(int id, int cantidad) throws RemoteException, SQLException, ProductNotFoundException {
		Statement statement = conn.createStatement();
		String query = "UPDATE stock SET stock = stock - %d WHERE idProducto = %d";
		query = String.format(query, cantidad, id);
		
		int rowCount = statement.executeUpdate(query);
		if (rowCount == 0) {
			throw new ProductNotFoundException(id);
		}
	}
	
	public Item obtenerItem(int idProducto) throws APIDownException, ProductNotFoundException {
		Map<String, String> params = new HashMap<>();
		params.put("id", Integer.toString(idProducto));
		HttpURLConnection conn = establishConnection("products" +  ParameterStringBuilder.getParamsString(params));

		int status = -1;

		try {
			status = conn.getResponseCode();
			if (status == 404) {
				throw new ProductNotFoundException(idProducto);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder content = new StringBuilder();
			while ((line = in.readLine()) != null) {
				content.append(line);
			}
			in.close();
			ObjectMapper om = new ObjectMapper();
			JsonNode base = om.readTree(content.toString());
			JsonNode commertialOffer = base.get("commertialOffer");

			Item item = new Item(
				idProducto,
				commertialOffer.get("priceWithoutDiscount").asInt(),
				commertialOffer.get("discountValue").asInt(),
				commertialOffer.get("price").asInt(),
				commertialOffer.get("quantity").asInt(),
				commertialOffer.get("packPrice").asInt(),
				base.get("productName").asText()
			);
			
			return item;
		} catch (IOException e) {
			if (status < 0) {
				throw new APIDownException();
			}
			throw new APIDownException();
		} finally {
			conn.disconnect();
		}


	}

	private HttpURLConnection establishConnection(String path) {
		try {
			URI apiUrl = new URI(apiUrlString + "/" + path);
			HttpURLConnection conn = (HttpURLConnection) apiUrl.toURL().openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(500);
			conn.setReadTimeout(500);
			return conn;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			System.err.println("Error al crear la conexion a la API (URL malformada)");
		}
		return null;
	}

	private Connection createConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();

		connectionProps.put("user", "root");
		connectionProps.put("password", "");
		try {
			conn = java.sql.DriverManager.getConnection("jdbc:mariadb://localhost:3306/", connectionProps);
		} catch (SQLException e) {
			System.err.println("Error al conectar a la base de datos");
			throw e;
		}
		return conn;
	}
	
	private String readFile(String path) throws IOException {
        StringBuilder contenido = new StringBuilder();

        File archivo = new File(path);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);

        String linea;
        while ((linea = br.readLine()) != null) {
            contenido.append(linea);
            contenido.append("\n");
        }

        br.close();

        return contenido.toString();
	}

	@Override
	public Usuario logIn(int id, int clave) throws RemoteException {
		String query = String.format("SELECT idUsuario, nombre, rol FROM usuarios WHERE idUsuario = %d AND clave = %d", id, clave);

		try {
			Statement statement = conn.createStatement();
			ResultSet data = statement.executeQuery(query);

			if (data.next() == false) {
				return null;
			}

			int rol = data.getInt("rol");
			String nombre = data.getString("nombre");
			return new Usuario(id, nombre, clave, rol);

		} catch (SQLException e) {
			e.getStackTrace();
			System.err.println("Error al ejecutar sentencia " + query);
		}
		return null;
	}

	@Override
	public ArrayList<Usuario> obtenerCajeros() throws RemoteException, SQLException {
		Statement statement = conn.createStatement();
		String query = "SELECT idUsuario, nombre, clave FROM usuarios WHERE rol = %d";
		query = String.format(query, Rol.CAJERO);
		
		ArrayList<Usuario> cajeros = new ArrayList<>();
		ResultSet data = statement.executeQuery(query);
		while(data.next()) {
			String nombre = data.getString("nombre");
			int clave = data.getInt("clave");
			int id = data.getInt("idUsuario");
			
			Usuario usuario = new Usuario(id, nombre, clave, Rol.CAJERO);
			cajeros.add(usuario);
			
		}
		return cajeros;
	}

	@Override
	public void agregarCajero(String nombre, int clave) throws RemoteException, SQLException {
		Statement statement = conn.createStatement();
		String query = "INSERT INTO usuarios(nombre, clave, rol) VALUES ('%s', %d, %d)";
		query = String.format(query, nombre, clave, Rol.CAJERO);
		
		statement.executeUpdate(query);
	}

	@Override
	public void eliminarCajero(int id) throws RemoteException, SQLException, CajeroNotFoundException {
		Statement statement = conn.createStatement();
		String query = "DELETE FROM usuarios WHERE idUsuario = %d AND rol = %d";
		query = String.format(query, id, Rol.CAJERO);
		
		int rowsAffected = statement.executeUpdate(query);
		if(rowsAffected == 0) {
			throw new CajeroNotFoundException(id);
		}
	}
	
}
