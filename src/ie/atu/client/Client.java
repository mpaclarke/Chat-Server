package ie.atu.client;

import java.io.*;
import java.net.*;

/**
 * 
 * @author Michael Clarke
 * @version 1.0
 * @since JavaSE-19
 * 
 *        This is the class that runs the client. This class also calls
 *        InputHandler to read in data from the console. The main function of
 *        this class is to connect the client to the server.
 * 
 * @see InputHandler
 */
public class Client implements Runnable {
	private String hostname;
	private int portNumber;
	protected PrintWriter printOut;
	private BufferedReader readIn;
	private Socket clientSocket;
	protected boolean done;

	/**
	 * Creates an instance of the client with a host name and a port number.
	 * Initializes the boolean that keeps while loops running in the class.
	 * 
	 * @param hostname   takes in the host name from the user
	 * @param portNumber takes in the port number from the user
	 */
	public Client(String hostname, int portNumber) {
		this.done = false;
		this.hostname = hostname;
		this.portNumber = portNumber;

	}

	/**
	 * Creates a socket to connect with the server. Creates and instance of
	 * InputHandler to read in data from the client.
	 */
	@Override
	public void run() {
		try {
			clientSocket = new Socket(hostname, portNumber);
			printOut = new PrintWriter(clientSocket.getOutputStream(), true);
			readIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			InputHandler ih = new InputHandler(this);
			Thread t = new Thread(ih);
			t.start();

			String inMessage;
			while ((inMessage = readIn.readLine()) != null) {
				if (inMessage.equals("closeClient")) {
					System.out.println(
							"[INFO] The server has closed down. \n[INFO] This client has closed its connection to Port: "
									+ portNumber);
					this.shutDown();
				} else {
					System.out.println(inMessage);
				}
			}

		} catch (UnknownHostException ex) {
			System.out.println("[INFO] Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("[INFO] I/O Error: " + ex.getMessage() + "\n[INFO] Check that the server is running...");
		}

	}

	/**
	 * Shuts down the client.
	 */
	public void shutDown() {
		this.done = true;
		try {
			readIn.close();
			printOut.flush();
			printOut.close();
			clientSocket.close();
			System.exit(0);
		} catch (Exception e) {
			System.out.println("[INFO] Error at shutdown: " + e.getMessage());
		}
	}

	/**
	 * Starts the client by calling run() and takes in a host name and a port
	 * number.
	 * 
	 * @param args takes in a host name and a port number as an argument.
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("[ERROR] Enter a valid hostname and port number to start the client.");
			System.exit(0);
		}

		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		Client c = new Client(hostname, port);
		c.run();
	}
}
