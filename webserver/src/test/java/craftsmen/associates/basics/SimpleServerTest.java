package craftsmen.associates.basics;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	private void stopServer() {
		server.stop();
	}

	private void thenAnswerContains(String answerExpected) throws Exception {
		assertTrue(readFromConection().contains(answerExpected));
	}

	private String readFromConection() throws IOException {
		return readerToString(connection.getInputStream());
	}

	private static String readerToString(InputStream input)
			throws IOException {
		BufferedReader isr2 = new BufferedReader(new InputStreamReader(input));
		
		StringBuilder builder = new StringBuilder();
		String read = "";
		do{
			read = isr2.readLine();
			builder.append(read);
		}while(isReadable(read));
		return builder.toString();
	}

	private static boolean isReadable(String read) {
		return (read != null && read.length()>1);
	}

	private URLConnection whenISendARequest() throws Exception {
		URL url = new URL("http://localhost:"+PORT);
		connection = url.openConnection();
		writeInConnection();
		
		return connection;
	}

	private void writeInConnection() throws IOException {
		connection.setDoOutput(true);
		PrintStream printStream = new PrintStream(connection.getOutputStream());
		printStream.println("GET / HTTP/1.1");
		printStream.println("");
		printStream.close();
	}

	private void whenServerIsLaunched() throws Exception {
		Executors.newSingleThreadExecutor().execute(server);
	}
}
