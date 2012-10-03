package craftsmen.associates.basics;

import static craftsmen.associates.utils.IOUtils.readerToString;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

public class SimpleServerTest {
	
	private static final int PORT = 8111;
	private URLConnection connection;
	private SimpleServer server;
	
	
	@Test(timeout=5000)
	public void serverAnswersHi() throws Exception {
		server = new SimpleServer(PORT);
		server.run();
		whenISendARequest();
		thenAnswerContains("hi");
		server.stop();
	}

	private void whenISendARequest() throws Exception {
		connection = new URL("http://localhost:"+PORT).openConnection();
		writeInConnection("GET / HTTP/1.1\n\n");
	}
	
	private void writeInConnection(String httpCommand) throws IOException {
		connection.setDoOutput(true);
		try(OutputStream outputStream = connection.getOutputStream()){
			outputStream.write(httpCommand.getBytes());
		}
	}

	private void thenAnswerContains(String answerExpected) throws Exception {
		assertTrue(readFromConection().contains(answerExpected));
	}
	private String readFromConection() throws IOException {
		return readerToString(connection.getInputStream());
	}

}