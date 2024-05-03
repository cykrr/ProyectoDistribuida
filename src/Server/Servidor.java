package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Common.InterfazServidor;
import Common.Item;
import Common.ItemBoleta;
import Common.Boleta;
// import JSON Jackson core

public class Servidor implements InterfazServidor {
	private static String apiUrlString = "http://localhost:5000";
	private Connection conn;
	JSONObject object = new JSONObject();

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
			System.out.println("Error al leer archivo " + path);
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
		String query = "SELECT cajeros.nombre, cajeros.idCajero, itemsBoleta.idProducto, itemsBoleta.precioTotal, itemsBoleta.cantidad "
				+ "FROM itemsBoleta JOIN boletas USING(idBoleta) JOIN cajeros USING(idCajero) WHERE idBoleta = %d";
		query = String.format(query, idBoleta);
		
		Statement statement = conn.createStatement();
		ResultSet data = statement.executeQuery(query);
		
		int columnCount = data.getMetaData().getColumnCount();
		if (columnCount == 0) {
			// ID no existe, lanzar excepción (?)
		}
		
		data.next();
		String nombreCajero = data.getString("nombre");
		int idCajero = data.getInt("idCajero");
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
	
	public int enviarBoleta(Boleta boleta) throws RemoteException {
		return 0;
	}
	
	public Item obtenerItem(int idProducto) throws APIDownException, ProductNotFoundException {
		Map<String, String> params = new HashMap<>();
		params.put("id", Integer.toString(idProducto));
		HttpURLConnection conn = establishConnection("products" +  ParameterStringBuilder.getParamsString(params));


		int status = -1;

		try {
			status = conn.getResponseCode();
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
				 commertialOffer.get("quantity").asInt(),
				  commertialOffer.get("price").asInt(),
				   commertialOffer.get("packPrice").asInt(),
				    commertialOffer.get("priceWithoutDiscount").asInt(),
					 commertialOffer.get("discountValue").asInt(),
					 base.get("productName").asText());
			return item;
		} catch (IOException e) {
			if (status == 404) {
				throw new ProductNotFoundException(idProducto);
			} else if (status < 0) {
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
			URL apiUrl  = new URL(apiUrlString + "/" + path);
			HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(500);
			conn.setReadTimeout(500);
			return conn;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.err.println("Error al crear la conexion a la API");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al crear la conexion a la API");
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

}
