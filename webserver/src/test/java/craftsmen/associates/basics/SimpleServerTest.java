package craftsmen.associates.basics;

import static craftsmen.associates.utils.IOUtils.readerToString;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;

import org.junit.Test;

public class SimpleServerTest {
	
	private static final int PORT = 8111;
	private SimpleServer server;
	
	@Test(timeout=5000)
	public void serverAnswersHi() throws Exception {
		whenServerIsRunning();
		URLConnection connection = whenISendARequest();
		thenAnswerContains(connection, "hi");
		stopServer();
	}

	private void stopServer() {
		server.stop();
	}

	private void whenServerIsRunning() {
		server = new SimpleServer(PORT);
		Executors.newSingleThreadExecutor().execute(server);
	}

	private URLConnection whenISendARequest() throws Exception {
		URLConnection connection  = new URL("http://localhost:"+PORT).openConnection();
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

	@Test(timeout=5000)
	public void serverAnswersHiAtEachRequest() throws Exception {
		whenServerIsRunning();
		URLConnection connection = whenISendARequest();
		thenAnswerContains(connection,"hi");
		URLConnection connection2 = whenISendARequest();
		thenAnswerContains(connection2,"hi");
		stopServer();
	}
	
}
