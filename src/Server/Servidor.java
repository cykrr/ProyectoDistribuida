package Server;
import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Parameter;
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
import java.util.Map;
import java.util.Properties;

import Common.InterfazServidor;
import Common.Boleta;

public class Servidor implements InterfazServidor    {
	static private String apiUrlString = "http://localhost:5000";

	public Servidor() throws IOException {
		super();
		UnicastRemoteObject.exportObject(this, 0);
		// test connection to API
		/*
		obtenerPrecio(1);
		testBD();
		*/
		crearBD();
	}
	
	private void crearBD() {
		Connection conn;
		String queries;
		
		try {
			conn = createConnection();
		} catch (SQLException e) {
			System.out.println("Error al conectar a la base de datos");
			System.out.println("Error: " + e.getMessage());
			return;
		}
		
		String path = "src/Server/db.sql";
		try {
			queries = readFile(path);
		} catch (IOException e) {
			System.out.println("Error al leer archivo " + path);
			System.out.println("Error: " + e.getMessage());
			return;
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
				System.out.println("Error: " + e.getMessage());
			}
        }
		
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

	private Connection createConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();

		connectionProps.put("user", "root");
		connectionProps.put("password", "");
		conn = java.sql.DriverManager.getConnection("jdbc:mariadb://localhost:3306/", connectionProps);
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
