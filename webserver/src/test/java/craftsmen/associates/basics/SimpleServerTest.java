package craftsmen.associates.basics;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
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

	@Test
	public void serverIsOK() throws Exception {
		whenServerIsLaunched();
		whenISendARequest();
		thenAnswerContains("hi");
		stopServer();
	}

	private void stopServer() {
		server.stop();
		
	}

	private void thenAnswerContains(String answerExpected) throws Exception {
		URL url = new URL("http://localhost:"+PORT);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		PrintStream printStream = new PrintStream(connection.getOutputStream());
		printStream.println("GET / HTTP/1.1");
		printStream.println("");
		printStream.close();
		
		InputStream inputStream = connection.getInputStream();
		BufferedReader isr = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = new StringBuilder();
		String read = "";
		do{
			read = isr.readLine();
			builder.append(read);
		}while(isReadable(read));
		
		System.out.println(builder.toString());
		assertTrue(builder.toString().contains(answerExpected));
		
	}

	private boolean isReadable(String read) {
		return (read != null && read.length()>1);
	}

	private void whenISendARequest() {
		// TODO Auto-generated method stub
		
	}

	private void whenServerIsLaunched() throws Exception {
		Executors.newSingleThreadExecutor().execute(server);
	}
}
