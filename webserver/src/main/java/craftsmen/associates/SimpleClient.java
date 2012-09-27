package craftsmen.associates;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SimpleClient {
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:8111/");
		URLConnection conn = openConnection(url);
		writeInConnection(conn);
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

	private static void readFromConnection(URLConnection conn)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      System.out.println("Reading response ");
	      String info = null;
	      while ((info = in.readLine()) != null) {
	        System.out.println("now got " + info);
	        if (info.equals(""))
	          break;
	      }
	     System.out.println("Response read");
		in.close();
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