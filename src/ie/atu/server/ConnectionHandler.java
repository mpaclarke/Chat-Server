package ie.atu.server;

import java.io.*;
import java.net.Socket;

/**
 * 
 * @author Michael Clarke
 * @version 1.0
 * @since JavaSE-19
 * 
 *        This class handles the individual client connections.
 * 
 * @see Server
 * 
 */
public class ConnectionHandler implements Runnable {
	private Socket clientSocket;
	private Server server;
	private BufferedReader readIn;
	protected PrintWriter printOut;
	private String userName;

	/**
	 * Creates an object that handles individual client objects.
	 * 
	 * @param clientSocket takes in the instance of the client socket from Server.
	 * @param server       takes in the instance of the Server to connect with
	 *                     methods held by that class.
	 */
	public ConnectionHandler(Socket clientSocket, Server server) {
		super();
		this.clientSocket = clientSocket;
		this.server = server;
	}

	/**
	 * Reads in data and prints out data. Collects user name from the client.
	 * Broadcasts messages from the client. Takes the command to close this instance
	 * of ConnectionHandler from the client.
	 */
	@Override
	public void run() {
		try {
			printOut = new PrintWriter(clientSocket.getOutputStream(), true);
			readIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			printOut.println("******* WELCOME TO CHAT APP 1.0 *******");
			printOut.println("[INFO] Enter your user name >");
			userName = readIn.readLine();

			server.broadcastMessage("[INFO] " + userName + " has joined the chat app...");

			String message;
			while ((message = readIn.readLine()) != null) {
				if (message.startsWith("\\q")) {
					server.broadcastMessage("[INFO] " + userName + " has left the Chat App.");
					this.shutDown();
				} else {
					server.broadcastMessage("[" + userName + "]" + ": " + message);
				}
			}
		} catch (Exception e) {
			System.out.println("[INFO] Error at connection handler: " + e.getMessage());
			this.shutDown();
		}
	}

	/**
	 * Sends the client's message to the output stream. ServerWriter also uses this
	 * method via the broadcast message methods in the Server class.
	 * 
	 * @param message from the client.
	 */
	public void postMessage(String message) {
		printOut.println(message);
	}

	/**
	 * Shuts down this instance of ConnectionHandler.
	 */
	public void shutDown() {
		try {
			readIn.close();
			printOut.flush();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("[INFO] Error at connection handler shutdown: " + e.getMessage());
		}
	}
}
