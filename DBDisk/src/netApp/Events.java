package netApp;

import java.net.Socket;
import java.util.EventListener;
import java.util.EventObject;

/**
 * Interface for the ServerListener
 * 
 * @author Gixbit
 *
 */
interface ServerListener extends EventListener {
	/**
	 * Describes Chat Events
	 * 
	 * @param e
	 *            {@link SocketEvent}
	 */
	public void chatEvent(SocketEvent e);
	
	/**
	 * Describes Disconnect Events
	 * 
	 * @param e
	 *            {@link SocketEvent}
	 */
	public void disconnectEvent(SocketEvent e);
	
	/**
	 * Describes Game Events
	 * 
	 * @param e
	 *            {@link SocketEvent}
	 */
	public void gameEvent(SocketEvent e);
	
	/**
	 * Describes {@link Connection} Events
	 * 
	 * @param e
	 *            {@link SocketEvent}
	 */
	public void connectEvent(SocketEvent e);
	
	/**
	 * Describes a response to a ping request from a {@link Client} or
	 * {@link Connection} in the point of view of the server
	 * 
	 * @param e
	 *            {@link SocketEvent}
	 */
	public void pongEvent(SocketEvent e);
}

/**
 * Interface for the ClientListener
 * 
 * @author Gixbit
 *
 */
interface ClientListener extends EventListener {
	/**
	 * Describes {@link Client}-related Chat Events received from the
	 * {@link Server}
	 * 
	 * @param e
	 *            {@link GUIEvent}
	 */
	public void chatEvent(GUIEvent e);
	
	/**
	 * Describes {@link Client}-related Window Events received from the
	 * {@link Server}
	 * 
	 * @param e
	 *            {@link GUIEvent}
	 */
	public void windowEvent(GUIEvent e);
	
	/**
	 * Describes {@link Client}-related Game Events received from the
	 * {@link Server}
	 * 
	 * @param e
	 *            {@link GUIEvent}
	 */
	public void gameEvent(GUIEvent e);
	
	/**
	 * Describes {@link Client}-related exit Events received from the
	 * {@link Server}
	 * 
	 * @param e
	 *            {@link GUIEvent}
	 */
	public void exitEvent(GUIEvent e);
	
	/**
	 * Describes {@link Connection} Event
	 * 
	 * @param e
	 *            {@link GUIEvent}
	 */
	public void connectEvent(GUIEvent e);
	
	/**
	 * Describes a ping request from the server
	 * 
	 * @param e
	 *            GUIEvent
	 */
	public void pingEvent(GUIEvent e);
}

/**
 * Describes a {@link GUIEvent} that is a subclass of the {@link EventObject}
 * superclass
 * 
 * @author Gixbit
 *
 */
@SuppressWarnings ("serial")
class GUIEvent extends EventObject {
	/**
	 * The subtype of this event
	 */
	private String Type;
	/**
	 * The data associated with this subclass of the {@link EventObject}
	 */
	private String Data;
	
	/**
	 * Create a {@link GUIEvent} with {@link Object}, String, String as
	 * parameters. The {@link Object} is the source and the two strings are the
	 * type and data to be manipulated.
	 * 
	 * @param source
	 *            Any {@link Object}
	 * @param type
	 *            Type String
	 * @param x
	 *            Data String
	 */
	public GUIEvent(Object source, String type, String x) {
		super(source);
		this.Type = type;
		this.Data = x;
	}
	
	/**
	 * Create a {@link GUIEvent} with {@link Object} and String as parameters.
	 * The {@link Object} is the source and the string is the type. Contains no
	 * data.
	 * 
	 * @param source
	 *            Any {@link Object}
	 * @param type
	 *            Type String
	 */
	public GUIEvent(Object source, String type) {
		super(source);
		this.Type = type;
	}
	
	/**
	 * Get and return the {@link Socket} associated with the event
	 * 
	 * @return {@link Socket} if not null
	 */
	public Socket getSocket() {
		if (source instanceof Client) {
			return ((Client)source).getSocket();
		} else
			return null;
	}
	
	/**
	 * Get and return the {@link Client} object for this event, if it exists,
	 * otherwise returns null
	 * 
	 * @return {@link Client}
	 */
	public Client getClient() {
		if (source instanceof Client) {
			return ((Client)source);
		} else {
			return null;
		}
	}
	
	/**
	 * Return a boolean stating whether or not the source of this event is a
	 * {@link Socket}.
	 * 
	 * @return <B>Boolean</B> True or False
	 */
	public boolean isSocket() {
		if (getSocket() == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Get and return the String containing the type of Event this
	 * {@link Object} describes
	 * 
	 * @return Type String
	 */
	public String getType() {
		return Type;
	}
	
	/**
	 * Get and return the String containing the data of this event
	 * 
	 * @return Data String
	 */
	public String getData() {
		return Data;
	}
}

/**
 * Describes a SocketEvent that is a subclass of the {@link EventObject}
 * superclass
 * 
 * @author Gixbit
 *
 */
@SuppressWarnings ("serial")
class SocketEvent extends EventObject {
	/**
	 * The subtype of this event
	 */
	private String Type;
	/**
	 * The data associated with this {@link EventObject}
	 */
	private String Data;
	
	/**
	 * Create a {@link SocketEvent} with {@link Object}, String, String as
	 * parameters. The {@link Object} is the source and the two strings are the
	 * type and data to be manipulated
	 * 
	 * @param source
	 *            Any {@link Object}
	 * @param type
	 *            Type String
	 * @param x
	 *            Data String
	 */
	public SocketEvent(Object source, String type, String x) {
		super(source);
		this.Type = type;
		this.Data = x;
	}
	
	/**
	 * Create a {@link SocketEvent} with {@link Object} and String as
	 * parameters. The {@link Object} is the source and the string is the type.
	 * Contains no data.
	 * 
	 * @param source
	 *            Any {@link Object}
	 * @param type
	 *            Type String
	 */
	public SocketEvent(Object source, String type) {
		super(source);
		this.Type = type;
	}
	
	/**
	 * Return a boolean stating whether or not the source of this event is a
	 * socket.
	 * 
	 * @return <B>Boolean</B> True or False
	 */
	public boolean isSocket() {
		if (getSocket() == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Get and return the {@link Connection} that triggered this event, if it
	 * exists, otherwise returns null
	 * 
	 * @return <B>{@link Connection}</B>
	 */
	public Connection getConnection() {
		if (source instanceof Connection) {
			return ((Connection)source);
		} else {
			return null;
		}
	}
	
	/**
	 * Get and return the socket associated with the event
	 * 
	 * @return Socket if not null
	 */
	public Socket getSocket() {
		if (source instanceof Connection) {
			return ((Connection)source).getSocket();
		} else
			return null;
	}
	
	/**
	 * Get and return the String containing the type of Event this
	 * {@link Object} describes
	 * 
	 * @return Type String
	 */
	public String getType() {
		return Type;
	}
	
	/**
	 * Get and return the String containing the data of this event
	 * 
	 * @return Data String
	 */
	public String getData() {
		return Data;
	}
}
