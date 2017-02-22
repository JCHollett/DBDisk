package netApp;

import java.net.Socket;

/**
 * This class is used for reading data received from {@link Client}
 * {@link Socket} or {@link Server} {@link Connection} {@link Socket}s and
 * manipulating event data created in {@link Client} or {@link Server}
 * 
 * @author Gixbit
 *
 */
public class Listener {
	/**
	 * Create the clientlistener for the clientthread
	 * 
	 * @param c
	 *            {@link ClientThread}
	 * @return <B>ClientListener</B>
	 */
	public static ClientListener clientListener(ClientThread c) {
		return new ClientListener() {
			/**
			 * The {@link ClientThread} that should be currently running for
			 * this listener to interact with
			 */
			private ClientThread client = c;
			
			/**
			 * How to handle a window event from the {@link Server}.
			 */
			@Override
			public void windowEvent(GUIEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * How to handle a game event from the {@link Server}
			 */
			@Override
			public void gameEvent(GUIEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * How to handle an exit event from the {@link Server}
			 */
			@Override
			public void exitEvent(GUIEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * How to handle a chat event from the {@link Server}
			 */
			@Override
			public void chatEvent(GUIEvent e) {
				ChatConsole.print(e.getData());
			}
			
			/**
			 * How to handle {@link Connection} events from the {@link Server}
			 * 
			 */
			@Override
			public void connectEvent(GUIEvent e) {
				ChatConsole.print(e.getData());
			}
			
			/**
			 * How to handle a ping request from the {@link Server}
			 * 
			 * @see HeartBeat
			 */
			@Override
			public void pingEvent(GUIEvent e) {
				e.getClient().sendData("Pong");
				
			}
			
		};
	}
	
	/**
	 * Create the ServerListener for the {@link ServerThread}
	 * 
	 * @param s
	 *            {@link ServerThread}
	 * @return <B>ServerListener</B>
	 */
	public static ServerListener serverListener(ServerThread s) {
		return new ServerListener() {
			/**
			 * The {@link ServerThread} that should be currently running for
			 * this listener to interact with
			 */
			private ServerThread server = s;
			
			/**
			 * How to handle chat events from {@link Connection}
			 * 
			 * @see Client
			 */
			@Override
			public void chatEvent(SocketEvent e) {
				DebugConsole.print(e.getSocket().getInetAddress().getHostName() + ": Chat Event");
				
				server.getServer().sendData("Chat" + ":" + e.getConnection().getUserName() + " >  " + e.getData());
			}
			
			/**
			 * How to handle disconnect events from {@link Connection}
			 * 
			 * @see Client
			 */
			@Override
			public void disconnectEvent(SocketEvent e) {
				
			}
			
			/**
			 * How to handle game events from {@link Connection}
			 * 
			 * @see Client
			 */
			@Override
			public void gameEvent(SocketEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * How to handle {@link Connection} events from {@link Client}
			 */
			@Override
			public void connectEvent(SocketEvent e) {
				if (e.getType().equalsIgnoreCase("Connect")) {
					server.getServer().sendData(
							"Connect" + ":" + e.getData().split(":")[0] + " has joined the server and set name to "
									+ e.getData().split(":")[1]);
				}
				
			}
			
			/**
			 * How to handle a ping response from a {@link Client}
			 * 
			 * @see HeartBeat
			 */
			@Override
			public void pongEvent(SocketEvent e) {
				// Not useless, Can be used to update parts of code that depend
				// on clients being connected or send updates to other clients
				// that responded to a heartbeat.
			}
		};
		
	}
}
