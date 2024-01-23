package ie.atu.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * 
 * @author Michael Clarke
 * @version 1.0
 * @since JavaSE-19
 * 
 *        This is the class that runs the server. This class also calls
 *        ConnectionHandler and ServerWriter. The main function of this class is
 *        to connect to the client class and create threads to handle multiple
 *        clients.
 * 
 * @see ConnectionHandler
 * @see ServerWriter
 */
public class Server implements Runnable {
	protected List<ConnectionHandler> users;
	private ServerSocket serverSocket;
	protected boolean done;
	private ExecutorService pool;
	private int portNumber;

	/**
	 * Creates an instance of the server with a port number. Initializes the array
	 * of users and the boolean that keeps while loops running in the class.
	 * 
	 * @param portNumber takes in the port number from the user
	 */
	public Server(int portNumber) {
		this.users = new ArrayList<ConnectionHandler>();
		this.done = false;
		this.portNumber = portNumber;
	}

	/**
	 * Creates a server socket, connects with the client and generate threads to
	 * handle multiple clients. Creates instances of ConnectionHandler and stores
	 * them in an ArrayList.
	 * 
	 * Additionally, this methods creates an instance of ServerWriter to write out
	 * to the clients.
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Listening at Port: " + portNumber);
			pool = Executors.newCachedThreadPool();

			ServerWriter sw = new ServerWriter(this);
			Thread t = new Thread(sw);
			t.start();

			while (!done) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("[INFO] Client connected from " + clientSocket.getInetAddress() + ": Port: "
						+ clientSocket.getPort());
				ConnectionHandler ch = new ConnectionHandler(clientSocket, this);
				users.add(ch);
				pool.execute(ch);
			}

		} catch (IOException ex) {
			System.out.println("[INFO] Error at server: " + ex.getMessage());
			shutDownServer();
		}
	}

	/**
	 * Broadcasts messages to required number of client connections by calling
	 * postMessage in ConnectionHandler. Prints message to the server's console.
	 * 
	 * @see ConnectionHandler
	 * @param message from the client or the server
	 */
	protected void broadcastMessage(String message) {
		for (ConnectionHandler ch : users) {
			if (ch != null)
				ch.postMessage(message);
		}
		System.out.println(message);
	}

	/**
	 * Broadcasts a code that tells the clients to disconnect.
	 */
	protected void broadcastCLose() {
		for (ConnectionHandler ch : users) {
			if (ch != null)
				ch.printOut.println("closeClient");
		}
	}

	/**
	 * Shuts down the server and any instances of ConnectionHandler.
	 * 
	 * @see ConnectionHandler
	 */
	protected void shutDownServer() {
		for (ConnectionHandler ch : users) {
			ch.shutDown();
		}
		this.done = true;
		this.pool.shutdown();

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("[INFO] Error at shutdown: " + e.getMessage());
		}

		System.out.println("[INFO] The Server has shutdown.");
	}

	/**
	 * Starts the server by calling run() and takes in a port number as an argument.
	 * 
	 * @param args takes in a port number as an argument.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("[ERROR] Enter a valid port number to start the server.");
			System.exit(0);
		}

		int PORT = Integer.parseInt(args[0]);
		Server server = new Server(PORT);
		server.run();
	}
}
