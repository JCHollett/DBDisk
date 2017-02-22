package netApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Client Class file. Used in operations for the client. Is also contained
 * within a {@link ClientThread}.
 * 
 * @author Gixbit
 *
 */
public class Client extends Thread {
	/**
	 * The username inherited from {@link ClientThread} creation or creating
	 * this object
	 */
	private String userName;
	/**
	 * The {@link ClientThread} this client uses
	 */
	private ClientThread client;
	/**
	 * The socket the client is opened on
	 */
	private Socket socket;
	/**
	 * The port the client is listening on
	 */
	private int port;
	/**
	 * The state of the client
	 */
	private boolean alive;
	/**
	 * The {@link ClientListener} associated with this client
	 */
	private ClientListener listener;
	
	/**
	 * Create a new client object with a {@link ClientThread},
	 * {@link InetAddress} and Port.
	 * 
	 * @param cThread
	 *            {@link ClientThread}
	 * @param address
	 *            {@link InetAddress}
	 * @param port
	 *            Integer
	 * @param name
	 *            String
	 * @throws IOException
	 *             Throws an error if the client cannot create a socket on the
	 *             specified address and port.
	 */
	public Client(ClientThread cThread, InetAddress address, int port, String name) throws IOException {
		super();
		this.port = port;
		socket = new Socket(address, this.port);
		
		alive = true;
		client = cThread;
		listener = Listener.clientListener(client);
		if (name != null) this.userName = name.trim();
		socket.getOutputStream().write(new String("Connect" + ":" + this.userName).getBytes());
	}
	
	/**
	 * This starts the thread for the client object
	 */
	@Override
	public void run() {
		byte[] buff;
		try {
			while (this.isRunning()) {
				if (socket.getInputStream().available() > 0) {
					buff = new byte[1024];
					socket.getInputStream().read(buff);
					String x = new String(buff).trim();
					
					this.createEvent(x);
				} else {
					// Nothing to read, statements here
				}
				
			}
		} catch (IOException e) {
			try {
				kill();
			} catch (IOException e1) {
				throw new RuntimeException("Critical Client error!");
			}
		}
	}
	
	/**
	 * This method is used to categorize data received before manipulating it in
	 * the event {@link Listener}
	 * 
	 * @param x
	 *            Data that was received from the <B>{@link Server}</B>
	 */
	private void createEvent(String x) {
		String[] y = x.split(":");
		if (y[0].equalsIgnoreCase("Chat")) {
			GUIEvent e = new GUIEvent(this, y[0], y[1]);
			listener.chatEvent(e);
		} else
			if (y[0].equalsIgnoreCase("Game")) {
				GUIEvent e = new GUIEvent(this, y[0], y[1]);
				listener.gameEvent(e);
			} else
				if (y[0].equalsIgnoreCase("Connect")) {
					GUIEvent e = new GUIEvent(this, y[0], y[1]);
					listener.connectEvent(e);
				} else
					if (y[0].equalsIgnoreCase("Ping")) {
						GUIEvent e = new GUIEvent(this, y[0]);
						listener.pingEvent(e);
					}
	}
	
	/**
	 * Get the port the client is connected on and return it as an integer.
	 * 
	 * @return <b>Integer</b>
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * This method is used to send data to the socket connected to the server.
	 * 
	 * @param x
	 *            The data to be sent to the server
	 */
	public void sendData(String x) {
		if (this.isRunning()) {
			try {
				this.socket.getOutputStream().write(x.getBytes());
			} catch (IOException e) {
				ChatConsole.print("Error: Send data exception");
			}
		}
	}
	
	/**
	 * This method returns the state of the connection. If the socket is alive,
	 * this will return true;
	 * 
	 * @return <b>Boolean</b> true or false
	 */
	public boolean isRunning() {
		return (alive && socket.isConnected() && super.isAlive());
	}
	
	/**
	 * Get the client socket and return it.
	 * 
	 * @return Socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Use this to shutdown the client socket. Please use the
	 * {@link ClientThread} to shutdown the Client.
	 * 
	 * @throws IOException
	 *             Fails to close socket
	 */
	public void kill() throws IOException {
		socket.close();
		
		this.alive = false;
		this.interrupt();
	}
	
	/**
	 * Get and return the username associated with the client
	 * 
	 * @return String
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * Get the {@link Listener} currently in use and return it.
	 * 
	 * @return <B>ClientListener</B>
	 */
	public ClientListener getListener() {
		return this.listener;
	}
}
