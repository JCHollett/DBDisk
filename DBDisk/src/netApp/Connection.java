package netApp;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Describes a connection made to the {@link Server} object
 * 
 * @author Gixbit
 *
 */
public class Connection extends Thread {
	/**
	 * The username that was entered in the GUI on connection; default is
	 * InetAddress hostname
	 */
	private String username;
	/**
	 * The socket this connection is opened on
	 */
	private Socket socket;
	/**
	 * The {@link ServerThread} this connection was made from
	 */
	private ServerThread server;
	
	/**
	 * This variable is exclusive to the HeartBeat object.
	 * 
	 * @see HeartBeat
	 */
	private boolean ping;
	/**
	 * The current state of the connection made to the {@link Server}
	 */
	private boolean alive;
	
	/**
	 * Create a Connection with Socket and {@link ServerThread} provided. Used
	 * by the {@link Server}.class.
	 * 
	 * @param ClientSocket
	 *            {@link Socket}
	 * @param s
	 *            {@link ServerThread}
	 */
	public Connection(Socket ClientSocket, ServerThread s) {
		super();
		server = s;
		this.socket = ClientSocket;
		this.alive = true;
		this.ping = true;
		this.username = this.socket.getInetAddress().getHostName();
	}
	
	/**
	 * Start the thread for connections to the {@link Server}
	 */
	@Override
	public void run() {
		byte[] buff;
		try {
			while (this.isRunning()) {
				buff = new byte[1024];
				if (socket.getInputStream().available() > 0) {
					socket.getInputStream().read(buff);
					String x = new String(buff).trim();
					createEvent(x);
					// Data sent from GUI would be read from the server in this
					// loop. This is a good place to create events when reading
					// from the {@link Client}s.
				} else {
					// If data wasn't available, do conditions here
				}
			}
		} catch (IOException e) {
			try {
				kill();
			} catch (IOException e1) {
				throw new RuntimeException("Critical {@link Client} Error");
			}
		}
	}
	
	/**
	 * This method is used to categorize data received before manipulating it in
	 * the {@link Listener}
	 * 
	 * @param x
	 *            Data that was received from the {@link Client}
	 */
	private void createEvent(String x) {
		String[] y = x.split(":");
		if (y[0].equalsIgnoreCase("Chat")) {
			SocketEvent e = new SocketEvent(this, y[0], y[1]);
			server.getListener().chatEvent(e);
		} else
			if (y[0].equalsIgnoreCase("Game")) {
				SocketEvent e = new SocketEvent(this, y[0], y[1]);
				server.getListener().gameEvent(e);
			} else
				if (y[0].equalsIgnoreCase("Connect")) {
					DebugConsole.print("UserName changed from " + this.username + " to: " + y[1]);
					SocketEvent e = new SocketEvent(this, y[0], (this.username + ":" + y[1]));
					this.username = y[1];
					server.getListener().connectEvent(e);
				} else
					if (y[0].equalsIgnoreCase("Pong")) {
						Date d = new Date();
						DebugConsole.print("(" + d.getMinutes() + ":" + d.getSeconds() + ")From " + this.getUserName() + ": Pong!");
						this.gotPingResponse();
						SocketEvent e = new SocketEvent(this, y[0]);
						server.getListener().pongEvent(e);
					}
	}
	
	/**
	 * Get and return the {@link Socket} this Connection listens on
	 * 
	 * @return {@link Socket}; The Connection made to the server is established
	 *         through this
	 */
	public Socket getSocket() {
		return this.socket;
	}
	
	/**
	 * This method returns the state of the Connection. If the {@link Socket} is
	 * alive, this will return true;
	 * 
	 * @return <b>Boolean</b> true or false
	 */
	public boolean isRunning() {
		return !socket.isClosed() && isAlive() && alive;
	}
	
	/**
	 * Use this to stop the Connection. This method is safe to use when
	 * iterating the list of Connections.
	 * 
	 * @throws IOException
	 *             Fails to close Connection
	 */
	public void kill() throws IOException {
		DebugConsole.print(socket.getInetAddress().getHostName() + ":" + socket.getPort() + " disconnected");
		this.alive = false;
		this.socket.close();
		this.interrupt();
	}
	
	/**
	 * Use this to stop the Connection. This method is unsafe to use when
	 * iterating the list of Connections. The primary use of this is to
	 * eliminate bad Connections when they occur.
	 * 
	 * @throws IOException
	 *             Fails to disconnect
	 */
	public void disconnect() throws IOException {
		server.getServer().getConnections().remove(this);
		this.alive = false;
		this.socket.close();
		this.interrupt();
	}
	
	/**
	 * Get and return the username associated with this Connection
	 * 
	 * @return String
	 */
	public String getUserName() {
		return this.username;
	}
	
	/**
	 * If the Connection {@link Object} responds to a ping request, the ping
	 * boolean is triggered to true. This should never be called outside of this
	 * class.
	 */
	private void gotPingResponse() {
		this.ping = true;
	}
	
	/**
	 * Get and return the result of a recent Ping response. This is a useless
	 * method for anything outside of the {@link HeartBeat} class.
	 * 
	 * @return Boolean
	 */
	public boolean getPingResponse() {
		return this.ping;
	}
	
	/**
	 * Reset the ping boolean until the next ping request. This is a useless
	 * method outside of the {@link HeartBeat} class.
	 */
	public void resetPing() {
		this.ping = false;
	}
	
	/**
	 * Execute a timeout on a {@link Client} if they did not respond to a ping
	 * request or some other exception. Also removes the connection from the
	 * list of connections being tracked. This is method is free to be used
	 * however, it is just a rehash of the {@link #disconnect()} method
	 * 
	 * @throws IOException
	 *             throws IOException if a disconnection is unsuccessful
	 * @see #disconnect()
	 */
	public void timeout() throws IOException {
		disconnect();
		DebugConsole.print(this.getUserName() + " has timed out!");
	}
}