package craftsmen.associates.basics;

import static craftsmen.associates.utils.IOUtils.readerToString;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleServerTest {
	
	private static final int PORT = 8111;
	private SimpleServer server;
	
	@Before
	public void setUp(){
		System.err.println("Launchin the server...");
		server = new SimpleServer();
		Executors.newSingleThreadExecutor().execute(server);
		System.err.println("Launched");
	}

	@After
	public void tearDown() throws Exception{
		System.err.println("Stoping the server...");
		server.stop();
		System.err.println("Stopped");
	}
	
	@Test(timeout=5000)
	public void serverAnswersHi() throws Exception {
		URLConnection connection = whenISendARequest();
		thenAnswerContains(connection, "hi");
	}

	public URLConnection whenISendARequest() throws Exception {
		URLConnection connection  = new URL("http://localhost:"+server.getPort()).openConnection();
		writeInConnection("GET / HTTP/1.1\n\n", connection);
		return connection;
	}
	
	private void writeInConnection(String httpCommand, URLConnection connection) throws IOException {
		connection.setDoOutput(true);
		try(OutputStream outputStream = connection.getOutputStream()){
			outputStream.write(httpCommand.getBytes());
		}
	}

	private void thenAnswerContains(URLConnection connection,String answerExpected) throws Exception {
		assertTrue(readFromConection(connection).contains(answerExpected));
	}
	
	private String readFromConection(URLConnection connection) throws IOException {
		return readerToString(connection.getInputStream());
	}

	@Test//(timeout=7000)
	public void serverAnswersHiAtEachRequest() throws Exception {
		Runnable runable = new Runnable() {
			@Override
			public void run() {
				URLConnection connection;
				try {
					connection = whenISendARequest();
					thenAnswerContains(connection,"hi");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		System.err.println("LAUNCHING REQUEST");
		ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(5);
		for(int i=0; i<10 ; i++){
			newCachedThreadPool.execute(runable);
		}
		// ugly waiting for end
		Thread.sleep(5000);
	}
	
}
