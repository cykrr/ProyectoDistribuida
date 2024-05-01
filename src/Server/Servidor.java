package Server;
import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Common.InterfazServidor;
import Common.Boleta;

public class Servidor implements InterfazServidor    {
	static private String apiUrlString = "http://localhost:5000";

	Servidor() throws IOException {
		super();
		UnicastRemoteObject.exportObject(this, 0);
		// test connection to API
		obtenerPrecio(1);
		testBD();

	}
	public int enviarBoleta(Boleta boleta) throws RemoteException {
		return 0;
	}
	public int obtenerPrecio(int idProducto) throws APIDownException, ProductNotFoundException {
		Map<String, String> params = new HashMap<>();
		params.put("id", Integer.toString(idProducto));
		HttpURLConnection conn = establishConnection("products" +  ParameterStringBuilder.getParamsString(params));


		conn.setDoOutput(true);
		
		conn.setConnectTimeout(500);
		conn.setReadTimeout(500);
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
			
			int pricePos = content.toString().indexOf("\"price\"") + 7; 
			String sub = content.toString().substring(pricePos);
			String subsub = sub.substring(sub.indexOf(":")+1, sub.indexOf(","));
			return Integer.parseInt(subsub);

		} catch (IOException e) {
			if (status != 200) {
				throw new ProductNotFoundException(idProducto);
			}
			throw new APIDownException();
		} finally {
			conn.disconnect();
		}


	}
	public int obtenerBoleta(int idBoleta) throws RemoteException {
		return 0;
	}
	public int modificarStock(int idProducto, int cantidad) throws RemoteException {
		return 0;
	}

	private HttpURLConnection establishConnection(String path) {
		try {
			URL apiUrl  = new URL(apiUrlString + "/" + path);
			HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
			conn.setRequestMethod("GET");
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
