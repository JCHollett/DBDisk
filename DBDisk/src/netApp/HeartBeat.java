package netApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/** The purpose of this class is to send pings to the {@link Client} and await their response. A
 * successfuly response is rewarded by not disconnecting the {@link Client}. This is a useless class
 * outside of a {@link Server}.
 *
 * @see ServerListener#pongEvent(SocketEvent)
 * @see ClientListener#pingEvent(GUIEvent)
 * @see Connection#timeout()
 * @author Gixbit */
public class HeartBeat extends Thread {
	/** The {@link ServerThread} object this {{@link Object} originated from */
	private ServerThread server;
	
	/** Create a HeartBeat object for the {@link Server}
	 * 
	 * @param s
	 *            {@link ServerThread} */
	public HeartBeat(ServerThread s) {
		this.server = s;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		synchronized (this) {
			while (server.isRunning()) {
				server.getServer().sendData("Ping");
				Date d = new Date();
				DebugConsole.print("(" + d.toString() + ")To Clients: Ping!");
				try {
					this.wait(5000); // Wait for responses
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (Connection c : ((ArrayList<Connection>)server.getServer().getConnections().clone())) {
					if (c.getPingResponse()) {
						c.resetPing();
					} else {
						try {
							c.timeout();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							DebugConsole.print(e.getCause().getMessage());
						}
					}
				}
			}
		}
	}
}
