package craftsmen.associates.basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer implements Runnable{
	private static final String HTML_HEADER = "HTTP/1.0 200 OK\nMIME_version:1.0\nContent_Type:text/html\n";
	private static final String HTML_CONTENT = "<html> <head></head><body> <h1> hi</h1></Body></html>";
	private boolean stop = false;
	private int port = 8111;
	private ServerSocket server;

	public SimpleServer() {
	}

	public SimpleServer(int port) {
		this.port = port;
	}

	public void run() {
		try {
			server = new ServerSocket(0);
			while (!stop) {
				Socket s = server.accept();
				read(s);
				write(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	int getPort(){
		return server.getLocalPort();
	}
	
	private void write(Socket s) throws IOException {
		PrintStream out = new PrintStream(s.getOutputStream());
		System.out.println(Thread.currentThread().getName()+" writing response ");
		out.print(HTML_HEADER);
		String c = HTML_CONTENT;
		out.println("Content_Length:" + c.length());
		out.println("");
		out.println(c);
		out.println("");	}

	private void read(Socket s) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println(Thread.currentThread().getName()+" reading request");
		String info = null;
		while ((info = in.readLine()) != null) {
			System.out.println(Thread.currentThread().getName()+"now got " + info);
			if (info.equals(""))
				break;
		}
	}

	public void stop() {
		this.stop = true;
	}

}