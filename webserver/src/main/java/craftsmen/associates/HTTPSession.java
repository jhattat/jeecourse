package craftsmen.associates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class HTTPSession implements Runnable {
	private Socket socket;

	public HTTPSession(Socket s) {
		socket = s;
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		try{
		readRequest(socket);
		sendResponse(socket);
		socket.close();
		}catch(Exception e){
			System.err.println("Error occurs during request processing");
		}
	}

	private void readRequest(Socket s) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		long currentThreadId = Thread.currentThread().getId();
		System.out.println(currentThreadId+" : Reading request");
		echoInputSream(in, currentThreadId);
		System.out.println(currentThreadId+" : Request read ");
	}

	private void echoInputSream(BufferedReader in, long currentThreadId)
			throws IOException {
		String info = null;
		while ((info = in.readLine()) != null) {
			System.out.println(currentThreadId+" : "+info);
			if (info.equals(""))
				break;
		}
	}

	private void sendResponse(Socket s) throws IOException {
		long currentThreadId = Thread.currentThread().getId();
		System.out.println(currentThreadId+" : Sending response");

		PrintStream out = new PrintStream(s.getOutputStream());
		out.println("HTTP/1.0 200 OK");
		out.println("MIME_version:1.0");
		out.println("Content_Type:text/html");
		String c = "<html> <head></head><body> <h1> hi</h1></Body></html>";
		out.println("Content_Length:" + c.length());
		out.println("");
		out.println(c);
		out.close();
		System.out.println(currentThreadId+" : Response Sent");

	}
}