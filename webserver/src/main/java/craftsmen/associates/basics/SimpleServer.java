package craftsmen.associates.basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	private boolean stop = false;

	public static void main(String[] args) throws Exception {
		new SimpleServer().run();
	}


	public void run() throws IOException {
		ServerSocket ss = new ServerSocket(8111);
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
			in.close();
			
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
			out.close();
			s.close();
		}
	}


	public void stop() {
		this.stop = true;
	}
}