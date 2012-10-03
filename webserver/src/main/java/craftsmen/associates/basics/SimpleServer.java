package craftsmen.associates.basics;

import java.net.ServerSocket;

public class SimpleServer implements Runnable{
	private boolean stop = false;
	private ServerSocket server;

	public void run() {
		try {
			server = new ServerSocket(0);
			while (!stop) {
				new HTTPRequest(server.accept()).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	int getPort(){
		return server.getLocalPort();
	}
	
	public void stop() {
		this.stop = true;
	}

}