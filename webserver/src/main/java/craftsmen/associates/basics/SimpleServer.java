package craftsmen.associates.basics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class SimpleServer implements Runnable {
	private boolean stop = false;
	private int port = 8111;

	public SimpleServer() {
	}

	public SimpleServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		Executors.newSingleThreadExecutor().execute(new SimpleServer());
	}

	public void run() {
		try {
			ServerSocket ss = new ServerSocket(port);
			while (!stop) {
				Socket s = ss.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				System.out.println("Reading request ");
				String info = null;
				while ((info = in.readLine()) != null) {
					System.out.println("now got " + info);
					if (info.equals(""))
						break;
				}

				PrintStream out = new PrintStream(s.getOutputStream());
				System.out.println("Request read ");
				out.println("HTTP/1.0 200 OK");
				out.println("MIME_version:1.0");
				out.println("Content_Type:text/html");
				String c = "<html> <head></head><body> <h1> hi</h1></Body></html>";
				out.println("Content_Length:" + c.length());
				out.println("");
				out.println(c);
				out.println("");

				// out.close();
				// in.close(); // close the socket !
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		this.stop = true;
	}

}