package netApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server Class file. Used in operations for the server. Is also contained
 * within a {@link ServerThread}
 * 
 * @author Gixbit
 *
 */
public class Server extends Thread {
	/**
	 * The {@link ServerThread} this server uses
	 */
	private ServerThread server;
	/**
	 * The {@link ServerListener} associated with this server
	 */
	private ServerListener listener;
	/**
	 * The {@link ServerSocket} the server uses to generate a new
	 * {@link Connection}
	 */
	private ServerSocket servSocket;
	/**
	 * The port the Server will listen on
	 */
	private int port;
	/**
	 * The state of the server
	 */
	private boolean alive;
	/**
	 * The list of {@link Connection} currently to the server
	 * 
	 * @see Client
	 */
	private ArrayList<Connection> connections;
	/**
	 * Checks {@link Connection}s to see if they are still active
	 * 
	 * @see HeartBeat
	 */
	private HeartBeat heartBeat;
	
	/**
	 * Create a server {@link Object} with {@link ServerThread} and port
	 * 
	 * @param sThread
	 *            {@link ServerThread}
	 * @param port
	 *            Integer
	 * @throws IOException
	 *             Fails when the socket cannot open
	 */
	public Server(ServerThread sThread, int port) throws IOException {
		super();
		this.port = port;
		this.alive = true;
		server = sThread;
		
		connections = new ArrayList<Connection>();
		servSocket = new ServerSocket(port);
		// servSocket.setSoTimeout(3000);
		listener = Listener.serverListener(server);
	}
	
	/**
	 * Start the {@link Thread} for the {@link Server} {@link Object}
	 */
	@Override
	public void run() {
		Connection connection = null;
		Socket socket = null;
		this.heartBeat = new HeartBeat(server);
		this.heartBeat.start();
		while (this.isRunning()) {
			try {
				DebugConsole.print("Listening for connections...");
				socket = servSocket.accept();
			} catch (IOException e) {
				// Bad Socket error
			}
			
			if (socket != null) {
				//
				if (isUnique(socket)) {
					connection = new Connection(socket, server);
					// Do events here to handle new connections
					connections.add(connection);
					connection.start();
					DebugConsole.print("New Connection: " + socket.getInetAddress().getHostName());
				}
			}
			
		}
		
	}
	
	/**
	 * Use this to shutdown the server {@link Socket}. Please use the
	 * {@link ServerThread} to shutdown the server.
	 * 
	 * @throws IOException
	 *             Fails to kill a connection
	 */
	public void kill() throws IOException {
		for (Connection c : connections) {
			c.kill();
		}
		connections.clear();
		this.servSocket.close();
		
		this.alive = false;
		this.interrupt();
		
	}
	
	/**
	 * This method is used to find duplicate {@link Connection}s before they're
	 * made. If this returns true, it means there is a {@link Socket} with
	 * similar data already opened.
	 * 
	 * @param s
	 *            The {@link Socket} to be tested
	 * @return <b>Boolean</b> True or False
	 */
	private boolean isUnique(Socket s) {
		boolean unique = true;
		for (Connection c : connections) {
			if (c.getSocket().getInetAddress() == s.getInetAddress()) unique = false;
		}
		return unique;
	}
	
	/**
	 * This method returns the state of the server. If the server is alive, this
	 * will return true;
	 * 
	 * @return <b>Boolean</b> true or false
	 */
	public boolean isRunning() {
		return (alive && !servSocket.isClosed() && super.isAlive());
	}
	
	/**
	 * Get and return the list of connected {@link Client}s as an
	 * {@link ArrayList}{@literal <}{@link Connection}{@literal >}
	 * 
	 * @return <B>ArrayList</B> of type {@link Connection}
	 */
	public ArrayList<Connection> getConnections() {
		return connections;
	}
	
	/**
	 * Get and return the {@link ServerSocket} that generates new
	 * {@link Connection}s from {@link Client}s.
	 * 
	 * @return <B>ServerSocket</B>
	 */
	public ServerSocket getServer() {
		return servSocket;
	}
	
	/**
	 * Get and return the port the server is currently being hosted on as an
	 * integer.
	 * 
	 * @return <B>Integer</B>
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Send the same data to all {@link Client}s connected.
	 * 
	 * @param x
	 *            The data to be sent
	 */
	public void sendData(String x) {
		if (!this.isRunning()) {
			return;
		} else {
			for (Connection c : connections) {
				try {
					c.getSocket().getOutputStream().write(x.getBytes());
				} catch (IOException e) {
					try {
						c.disconnect();
					} catch (IOException e1) {
						throw new RuntimeException("Bad Socket Error");
					}
					DebugConsole.print("Error: Could not send data to" + c.getSocket().getInetAddress().getHostName());
				}
			}
			DebugConsole.print("Event: SentDataAll(" + x + ")");
		}
	}
	
	/**
	 * Sends data to a single {@link Client}
	 * 
	 * @param x
	 *            The data to be sent
	 * @param c
	 *            The opened {@link Connection} to have data sent to
	 * @throws IOException
	 *             when connection is invalid
	 */
	public void sendData(String x, Connection c) throws IOException {
		if (this.isRunning()) {
			return;
		} else {
			try {
				c.getSocket().getOutputStream().write(x.getBytes());
			} catch (IOException e) {
				DebugConsole.print("Error: Could not send data to" + c.getSocket().getInetAddress().getHostName());
				c.timeout();
			}
		}
	}
	
	/**
	 * Get the {@link ServerListener} currently in use and return it
	 * 
	 * @return <B> ServerListener </B>
	 */
	public ServerListener getListener() {
		return listener;
	}
}
