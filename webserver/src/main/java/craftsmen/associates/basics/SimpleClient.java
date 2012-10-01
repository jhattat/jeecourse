package craftsmen.associates.basics;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SimpleClient {
	public static void main(String[] args) throws Exception {
		new SimpleClient().run();
	}

	private  void run() throws MalformedURLException, IOException {
		URL url = new URL("http://localhost:8111/");
		URLConnection conn = openConnection(url);
		writeInConnection(conn);
		System.out.println(readFromConnection(conn));
	}

	private static URLConnection openConnection(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		return conn;
	}

	private String readFromConnection(URLConnection conn) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      System.out.println("Reading response ");
	      StringBuilder finalResponse = new StringBuilder();
	      String line = null;
	      while ((line = in.readLine()) != null) {
	        finalResponse.append(line);
	        if (line.equals(""))
	          break;
	      }
	     System.out.println(String.format("Response read %s",finalResponse.toString()));
		in.close();
		return finalResponse.toString();
	}

	private static void writeInConnection(URLConnection conn)
			throws IOException {
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		String content = "CONTENT=HELLO JSP !&ONEMORECONTENT =HELLO POST !";
		out.writeBytes(content);
		out.flush();
		out.close();
	}
}