package netApp;

import java.io.IOException;
import java.net.InetAddress;

/**
 * ClientThread class used for creating a separate thread from the
 * {@link Client} to do less specific and more general operations. Oversees the
 * {@link Client}.
 * 
 * @see Client
 * 
 * @author Gixbit
 *
 */
public class ClientThread extends Thread {
	/**
	 * The username to give to the client, default is the {@link InetAddress}
	 * hostname.
	 */
	private String userName;
	/**
	 * The port this ClientThread will start a {@link Client} object with
	 */
	private int port;
	/**
	 * The {@link Client} object this thread will create, default is null
	 */
	private Client client = null;
	/**
	 * The string this ClientThread will connect to, default is localhost
	 */
	private String address = "127.0.0.1";
	
	/**
	 * Create a ClientThread with an address of string and port with type
	 * integer, along with a username if provided
	 * 
	 * @param address
	 *            String
	 * @param port
	 *            Integer
	 * @param name
	 *            String
	 */
	public ClientThread(String address, int port, String name) {
		this.address = address.trim();
		this.port = port;
		if (name != null) this.userName = name.trim();
	}
	
	/**
	 * Creates a loopback thread on the port provided as an integer, along with
	 * a username if provided
	 * 
	 * @param port
	 *            Integer
	 * @param name
	 *            String
	 */
	public ClientThread(int port, String name) {
		this.port = port;
		this.userName = name.trim();
	}
	
	/**
	 * Run the ClientThread which starts a Client inside of it.
	 */
	@Override
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(this.address);
			client = new Client(this, addr, port, userName);
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
			stopThread();
			return;
		}
		while (this.isRunning()) {
			
		}
		return;
	};
	
	/**
	 * Completely shutdown the {@link Client}.
	 */
	public void stopThread() {
		try {
			client.kill();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.interrupt();
	}
	
	/**
	 * Get the {@link Client} socket and return it's thread.
	 * 
	 * @return Returns <B>Server</B>
	 */
	public Client getClient() {
		return this.client;
	}
	
	/**
	 * This method returns the state of the {@link Connection}. If the socket is
	 * alive, this will return true;
	 * 
	 * @return <b>Boolean</b> true or false
	 */
	public boolean isRunning() {
		return (client.isRunning() && !this.isInterrupted());
	}
	
	/**
	 * Get and return the {@link ClientListener} for the {@link Client}.
	 * 
	 * @return <B>{@link ClientListener}</B>
	 */
	public ClientListener getListener() {
		return client.getListener();
	}
}