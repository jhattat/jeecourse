package craftsmen.associates.basics;

import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiThreadedServer {
    /**
     * Pool de 5+1 thread.
     */
    private static final Executor worker = Executors.newFixedThreadPool(6);

    public static void mainLoop() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(1980);
        } catch (Exception cause) {
            System.err.println("Can not create server socket :'(");
            cause.printStackTrace(System.err);
        }
        while (server != null) {
            try {
                worker.execute(new HTTPRequest(server.accept()));
            } catch (Exception cause) {
                System.err.println("Error accepting client :'(");
                cause.printStackTrace(System.err);
            }
        }
    }

    public static void main(String[] args) {
        worker.execute(new Runnable() {
            @Override
            public void run() {
                mainLoop();
            }
        });
    }
}
