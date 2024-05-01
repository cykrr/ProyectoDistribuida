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
import java.util.HashMap;
import java.util.Map;

import Common.InterfazServidor;
import Common.Boleta;

public class Servidor implements InterfazServidor    {
	static private String apiUrlString = "http://localhost:5000";

	Servidor() throws IOException {
		super();
		UnicastRemoteObject.exportObject(this, 0);
		obtenerPrecio(1);
	}
	public int enviarBoleta(Boleta boleta) throws RemoteException {
		return 0;
	}
	public int obtenerPrecio(int idProducto) throws RemoteException {
		Map<String, String> params = new HashMap<>();
		params.put("id", Integer.toString(idProducto));
		HttpURLConnection conn = establishConnection("products" +  ParameterStringBuilder.getParamsString(params));


		conn.setDoOutput(true);
		
			conn.setConnectTimeout(500);
			conn.setReadTimeout(500);

		try {
			int status = conn.getResponseCode();
			System.out.println("Status: " + status);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder content = new StringBuilder();
			while ((line = in.readLine()) != null) {
				content.append(line);
			}
			in.close();
			assert status == 200;
			int pricePos = content.toString().indexOf("\"price\"") + 7; 
			String sub = content.toString().substring(pricePos);
			String subsub = sub.substring(sub.indexOf(":")+1, sub.indexOf(","));
			return Integer.parseInt(subsub);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al obtener el precio del producto");
			return -1;
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

}
