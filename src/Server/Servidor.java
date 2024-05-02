package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Common.InterfazServidor;
import Common.Item;
import Common.Boleta;
// import JSON Jackson core

public class Servidor implements InterfazServidor    {
	static private String apiUrlString = "http://localhost:5000";

	Servidor() throws IOException {
		UnicastRemoteObject.exportObject(this, 0);
		// test connection to API
		try {
			obtenerItem(1);
		} catch (APIDownException e) {
			System.err.println("API is down. Server will not be able to function properly.");
		} try {
			testBD();
		} catch (BDDownException e) {
			System.err.println("Database is down. Server will not be able to function properly.");
		}
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
	public Boleta obtenerBoleta(int idBoleta) throws RemoteException {
		return new Boleta();
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

	private void testBD() throws BDDownException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		// Assume default XAMPP mysql creds
		connectionProps.put("user", "root");
		connectionProps.put("password", "");
		try {
			conn = java.sql.DriverManager.getConnection("jdbc:mariadb://localhost:3306/", connectionProps);
		} catch (java.sql.SQLException e) {
			System.out.println("Error al conectar a la base de datos");
			System.out.println("Error: " + e.getMessage());
			throw new BDDownException();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
