package craftsmen.associates.basics;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleThreadServer {
	public static void main(String[] args) throws Exception {
		final ServerSocket serverSocket = new ServerSocket(8111);
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						new HTTPRequest(serverSocket.accept()).start();
					}
				} catch (IOException ioe) {
				}
			}
		});
		t.setDaemon(true);
		t.start();
		while (true) {}
	}
}