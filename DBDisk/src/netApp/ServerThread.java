package netApp;

import java.io.IOException;

/**
 * ServerThread class used for creating a separate {@link Thread} from the
 * {@link Server} to do less specific and more general operations. Oversees the
 * {@link Server}.
 * 
 * @author Gixbit
 *
 */
public class ServerThread extends Thread {
	/**
	 * The server object this thread will create, default is null
	 */
	private Server server = null;
	/**
	 * The loopback thread created for servers with loopback connections
	 */
	private ClientThread loopback;
	/**
	 * The port this ServerThread will start a server object with
	 */
	private int port = 5555;
	
	/**
	 * Create a ServerThread with a port and client thread for loopback
	 * connection
	 * 
	 * @param p
	 *            Integer
	 * @param lb
	 *            ClientThread
	 */
	public ServerThread(int p, ClientThread lb) {
		if (lb != null) loopback = lb;
		port = p;
	}
	
	/**
	 * Create a ServerThread with the default port (5555) and clien tthread for
	 * loopback connection
	 * 
	 * @param lb
	 *            ClientThread
	 */
	public ServerThread(ClientThread lb) {
		if (lb != null) loopback = lb;
	}
	
	/**
	 * Create a ServerThread with a port and no loopback connections
	 * 
	 * @param p
	 *            Integer
	 */
	public ServerThread(int p) {
		port = p;
	}
	
	/**
	 * Create a ServerThread wit the default port and no loopback connections.
	 */
	public ServerThread() {
		
	}
	
	/**
	 * Run the ServerThread which starts a server inside of it.
	 */
	@Override
	public void run() {
		try {
			server = new Server(this, port);
			server.start();
		} catch (IOException e) {
			stopThread();
			return;
		}
		while (this.isRunning()) {
			
		}
		return;
	}
	
	/**
	 * Completely shutdown the server.
	 */
	public void stopThread() {
		try {
			if (loopback.isRunning()) {
				loopback.stopThread();
			}
			
			server.kill();
			
		} catch (IOException e) {
			DebugConsole.print("Error: Server shutdown exception");
		}
		this.interrupt();
	}
	
	/**
	 * Get the server socket and return it's thread.
	 * 
	 * @return Returns <B>Server</B>
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * This method returns the state of the server. If the server is alive, this
	 * will return true;
	 * 
	 * @return <b>Boolean</b> true or false
	 */
	public boolean isRunning() {
		return (server.isRunning() && !this.isInterrupted());
	}
	
	/**
	 * Get and return the {@link ServerListener} associated with the
	 * {@link Server}.
	 * 
	 * @return <B>Listener</B>
	 */
	public ServerListener getListener() {
		return server.getListener();
	}
}
