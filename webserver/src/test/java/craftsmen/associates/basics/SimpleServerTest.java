package craftsmen.associates.basics;

import static craftsmen.associates.utils.IOUtils.readerToString;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleServerTest {
	
	
	private static final class Worker extends SimpleServerTest implements Runnable {
		CountDownLatch startSignal;
		CountDownLatch stopSignal;
		private int port;
		
		public Worker(CountDownLatch start,CountDownLatch stop, int port) {
			this.startSignal = start;
			this.stopSignal = stop;
			this.port = port;
		}

		@Override
		public void run() {
			try {
				startSignal.await();
				URLConnection connection;
				connection = whenISendARequest(port);
				thenAnswerContains(connection,"hi");
				stopSignal.countDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

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
		URLConnection connection = whenISendARequest(server.getPort());
		thenAnswerContains(connection, "hi");
	}

	public URLConnection whenISendARequest(int port) throws Exception {
		System.err.println("Client "+Thread.currentThread().getId()+" launched a request");
		URLConnection connection  = new URL("http://localhost:"+port).openConnection();
		int random =new Random().nextInt(1001);
		String parameters = String.format("time=%d",random);
		writeInConnection(String.format("POST / HTTP/1.1\n\nContent-Length: %d\n\n%s\n",parameters.length(), parameters), connection);
		return connection;
	}
	
	public void writeInConnection(String httpCommand, URLConnection connection) throws IOException {
		connection.setDoOutput(true);
		try(OutputStream outputStream = connection.getOutputStream()){
			outputStream.write(httpCommand.getBytes());
		}
	}

	public void thenAnswerContains(URLConnection connection,String answerExpected) throws Exception {
		System.out.println("Client"+Thread.currentThread().getId()+" is reading request" );
		assertTrue(readFromConection(connection).contains(answerExpected));
	}
	public String readFromConection(URLConnection connection) throws IOException {
		return readerToString(connection.getInputStream());
	}

	@Test(timeout=300)
	public void serverAnswersHiAtEachRequest() throws Exception {
		ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(5);
		CountDownLatch startSignal= new CountDownLatch(1);
		CountDownLatch stopSignal= new CountDownLatch(10);
		for(int i=0; i<10 ; i++){
			newCachedThreadPool.execute(new Worker(startSignal,stopSignal, server.getPort()));
		}
		startSignal.countDown(); // all are waiting for that !
		stopSignal.await();
	}
	
}
