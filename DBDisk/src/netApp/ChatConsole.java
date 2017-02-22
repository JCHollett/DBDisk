package netApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/** Chat console for {@link Server} and {@link Client} {@link Thread}s.
 *
 * @author Gixbit */
@SuppressWarnings("serial")
public class ChatConsole extends JFrame implements ActionListener {
	/** The area where messages from the {@link Server} and other {@link Client}s
	 * or {@link Connection}s are stored */
	private static JTextArea console = new JTextArea();
	
	/** Use this method to print messages to the ChatConsole
	 *
	 * @param x
	 *            String contains the data to append to the console */
	protected static void print(String x) {
		console.append(x + "\n");
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	/** The font used for the buttons */
	private Font buttonFont = new Font("Arial", Font.BOLD, 12);
	/** The field that entered text from the user gets put into */
	private JTextField consoleField = new JTextField();
	/** The {@link ClientThread} that governs this ChatConsole {@link Object}. */
	private ClientThread CT = null;
	/** The button used to return the menu */
	private JButton menu = new JButton("Back");
	/** The button used for quitting the program */
	private JButton quit = new JButton("Close");

	/** Creates a new ChatConsole object with the {@link ClientThread} that
	 * governs the {@link Connection} to the {@link Server}
	 *
	 * @param thread
	 *            {@link ClientThread} */
	public ChatConsole(ClientThread thread) {
		this.CT = thread;
		this.setName("ChatConsole");
		this.setTitle("ChatConsole");
		this.getContentPane().setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(640, 320));
		console.setEditable(false);
		this.consoleField.setSize(640, 32);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.quit.setFont(this.buttonFont);
		this.menu.setFont(this.buttonFont);
		JPanel south = new JPanel();
		south.add(this.consoleField);
		south.setLayout(new GridLayout(2, 1));
		JPanel subSouth = new JPanel();
		subSouth.setLayout(new GridLayout(1, 2));
		subSouth.add(this.menu);
		subSouth.add(this.quit);
		south.add(subSouth);
		JScrollPane sp = new JScrollPane(console);
		this.getContentPane().add(sp, BorderLayout.CENTER);
		this.getContentPane().add(south, BorderLayout.SOUTH);
		this.quit.addActionListener(this);
		this.menu.addActionListener(this);
		this.consoleField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String x = ((JTextField)e.getSource()).getText();
				ChatConsole.this.CT.getClient().sendData("Chat" + ":" + x);
				((JTextField)e.getSource()).setText("");
			}
		});
		// quit.addActionListener(this);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.menu) {
			if (this.CT != null && this.CT.isAlive())
				this.CT.stopThread();
			this.dispose();
		} else if (e.getSource() == this.quit) {
			if (this.CT != null && this.CT.isAlive())
				this.CT.stopThread();
			this.dispose();
		} else {
			// ???
		}
		console = null;
	}
}
