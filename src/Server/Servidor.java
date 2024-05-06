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
import Common.BDDownException;
import Common.Boleta;
import Common.BoletaNotFoundException;
import Common.InterfazServidor;
import Common.InvalidCredentialsException;
import Common.Item;
import Common.ItemBoleta;
import Common.ItemCarrito;
import Common.ProductNotFoundException;
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
		
		// SOLO PARA PRUEBAS
		// MOSTRAR BOLETA DEBERÍA LLAMARSE DESDE CLIENTE
		/*
		try {
			Boleta boleta = obtenerBoleta(1);
			System.out.println("Nombre cajero: " + boleta.getNombreCajero());
			Iterator<ItemBoleta> it = boleta.getItems();
			while(it.hasNext()) {
				ItemBoleta item = it.next();
				System.out.println("ID Prod: " + item.getIdProducto());
				System.out.println("Nombre Prod: " + item.getNombreProducto());
				System.out.println("Cantidad: " + item.getCantidad());
				System.out.println("Precio: " + item.getPrecioTotal() + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
		ArrayList<ItemCarrito> itemsCarrito = new ArrayList<>();
		
		Item item1 = new Item(4, 3000, 10, 2700, 0, 0, "fideos");
		Item item2 = new Item(5, 1000, 0, 1000, 2, 1800, "arroz");
		ItemCarrito itemCarrito1 = new ItemCarrito(item1, 2);
		ItemCarrito itemCarrito2 = new ItemCarrito(item2, 7);
		itemsCarrito.add(itemCarrito1);
		itemsCarrito.add(itemCarrito2);
		
		try {
			generarBoleta(itemsCarrito, 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		// test connection to API
		try {
			obtenerItem(1);
		} catch (APIDownException e) {
			System.err.println("API is down. Server will not be able to function properly.");
		} try {
			// testBD();
		} catch (BDDownException e) {
			System.err.println("Database is down. Server will not be able to function properly.");
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
	
	public Boleta obtenerBoleta(int idBoleta) throws SQLException {
		String query = "SELECT usuarios.nombre, usuarios.idUsuario, itemsBoleta.idProducto, itemsBoleta.precioTotal, itemsBoleta.cantidad "
				+ "FROM itemsBoleta JOIN boletas USING(idBoleta) JOIN usuarios USING(idUsuario) WHERE idBoleta = %d";
		query = String.format(query, idBoleta);
		
		Statement statement = conn.createStatement();
		ResultSet data = statement.executeQuery(query);
		
		int columnCount = data.getMetaData().getColumnCount();
		if (columnCount == 0) {
			throw new BoletaNotFoundException(idBoleta);
		}
		
		data.next();
		String nombreCajero = data.getString("nombre");
		int idCajero = data.getInt("idUsuario");
		Boleta boleta = new Boleta(idCajero, nombreCajero);
		
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
	
	public void generarBoleta(ArrayList<ItemCarrito> itemsCarrito, int idCajero) throws RemoteException, SQLException {
		try {
			// Empezar transacción
			conn.setAutoCommit(false);
			
			// Crear idBoleta y asignar a idCajero
			String query = "INSERT INTO boletas(idUsuario) VALUES (%d)";
			query = String.format(query, idCajero);
			
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
				int precioTotal = calcularPrecioTotal(item, cantidad);
				
				currentQuery = String.format(queryItem, idBoleta, item.getId(), precioTotal, cantidad);
				statement.executeQuery(currentQuery);
				
				// Actualizar stock
				currentQuery = String.format(queryStock, cantidad, item.getId());
				statement.executeUpdate(currentQuery);
			}
			
			// Finalizar transacción
			conn.commit();
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
	
	private int obtenerStock(int id) throws SQLException, ProductNotFoundException {
		Statement s = conn.createStatement();
		String q = "SELECT stock FROM stock WHERE idProducto = " + id;
		ResultSet rs = s.executeQuery(q);
		if (!rs.next()) {
			throw new ProductNotFoundException(id);
		}
		return rs.getInt("stock");
	}

	private int calcularPrecioTotal(Item item, int cantidad) {
		if(item.getCantidadPack() == 0) {
			return cantidad * item.getPrecioDescuento();
		}
		int cantidadPromo = cantidad / item.getCantidadPack();
		int resto = cantidad % item.getCantidadPack();
		return cantidadPromo * item.getPrecioPack() + resto * item.getPrecioDescuento();
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
	
	public int modificarStock(int idProducto, int cantidad) throws RemoteException {
		return 0;
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
	public Usuario logIn(int id, int clave) throws RemoteException, InvalidCredentialsException {
		String query = String.format("SELECT idUsuario, nombre, rol FROM usuarios WHERE idUsuario = %d AND clave = %d", id, clave);
		ResultSet data = null;
		try {
			Statement statement = conn.createStatement();
			data = statement.executeQuery(query);
			if (data == null) {
				return null;
			} else {
				int columnCount = data.getMetaData().getColumnCount();
				if (columnCount == 0) {
					throw new InvalidCredentialsException(id);
				}
			
				data.next();
				if (data.getInt("idUsuario") == id) {
					int rol = data.getInt("rol");
					String nombre = data.getString("nombre");
					return new Usuario(id, nombre, rol);
				}
				return null;

			}
		} catch (SQLException e) {
			e.getStackTrace();
			System.err.println("Error al ejecutar sentencia " + query);
		}
		return null;
	}
}
