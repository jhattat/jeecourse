package craftsmen.associates;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Serveur {
public static void main(String[] args) {
	try {
		HttpServer httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1",9898),5);
		httpServer.start();
		
	} catch (IOException e) {
		e.printStackTrace();
	}	
}
}
