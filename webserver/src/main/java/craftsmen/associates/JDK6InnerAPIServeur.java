package craftsmen.associates;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class JDK6InnerAPIServeur {
public static void main(String[] args) {
	try {
		HttpServer httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1",9898),5);
		httpServer.start();
		
		HttpHandler handler = new HttpHandler(){
				public void handle(HttpExchange exchange) throws IOException {
		            byte[] response = "I am the king !".getBytes();
		            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
		            exchange.getResponseBody().write(response);
		            exchange.close();
		        }
		};
		httpServer.createContext("/",handler);
	} catch (IOException e) {
		e.printStackTrace();
	}	
}
}
