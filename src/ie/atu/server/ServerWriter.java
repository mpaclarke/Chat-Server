package ie.atu.server;

import java.io.*;

/**
 * 
 * @author Michael Clarke
 * @version 1.0
 * @since JavaSE-19
 * 
 *        This class lets the server admin write to the chat application.
 *
 */
public class ServerWriter implements Runnable {
	private Server server;
	private BufferedReader messageReader;

	/**
	 * Takes in a instance of the server.
	 * 
	 * @see Server
	 * @param server takes in an instance of the Server class.
	 */
	public ServerWriter(Server server) {
		this.server = server;

	}

	/**
	 * Reads in text from the server console. Facilitates the shutdown command.
	 */
	@Override
	public void run() {
		try {
			messageReader = new BufferedReader(new InputStreamReader(System.in));
			while (!server.done) {
				String message = messageReader.readLine();
				if (message.equals("\\close")) {
					server.broadcastMessage("[INFO] The Server is shutting down...");
					server.broadcastCLose();
					server.shutDownServer();
				} else {
					server.broadcastMessage("[SERVER]: " + message);
				}
			}
		} catch (Exception e) {
			System.out.println("[INFO] Error at server writer: " + e.getMessage());
			server.shutDownServer();
		}
	}
}
