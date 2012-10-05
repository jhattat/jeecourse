package craftsmen.associates.basics;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer implements Runnable{
	private boolean stop = false;
	private ServerSocket server;
	private ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);

	public void run() {
		try {
			server = new ServerSocket(0);
			while (!stop) {
				newFixedThreadPool.execute(new HTTPRequest(server.accept()));
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