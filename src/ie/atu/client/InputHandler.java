package ie.atu.client;

import java.io.*;

/**
 * 
 * @author Michael Clarke
 * @version 1.0
 * @since JavaSE-19
 * 
 *        This class lets the client write to the chat application.
 */
public class InputHandler implements Runnable {
	private Client client;
	private BufferedReader messageReader;

	/**
	 * Takes in an instance of the client class.
	 * 
	 * @param client takes in an instance of client.
	 */
	public InputHandler(Client client) {
		this.client = client;
	}

	/**
	 * Reads in text from the server console. Facilitates the shutdown command.
	 */
	@Override
	public void run() {
		try {
			messageReader = new BufferedReader(new InputStreamReader(System.in));
			while (!client.done) {
				String message = messageReader.readLine();
				if (message.equals("\\q")) {
					client.printOut.println(message);
					messageReader.close();
					client.shutDown();
				} else {
					client.printOut.println(message);
				}
			}
		} catch (Exception e) {
			System.out.println("[INFO] Error at input reader: " + e.getMessage());
			client.shutDown();
		}
	}
}
