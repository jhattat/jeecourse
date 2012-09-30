package craftsmen.associates.basics;

import static craftsmen.associates.basics.IOUtils.readerToString;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;

import org.junit.Test;

public class SimpleServerTest {
	
	private static final int PORT = 8111;
	private SimpleServer server=new SimpleServer(PORT);
	private URLConnection connection;

	@Test
	public void serverAnswersHi() throws Exception {
		whenServerIsLaunched();
		whenISendARequest();
		thenAnswerContains("hi");
		stopServer();
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

	private void stopServer() {
		server.stop();
	}


	private void whenServerIsLaunched() throws Exception {
		Executors.newSingleThreadExecutor().execute(server);
	}
}
