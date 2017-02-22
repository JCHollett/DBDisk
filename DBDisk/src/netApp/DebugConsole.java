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

/** The DebugConsole for the {@link ServerThread}
 *
 * @author Gixbit */
@SuppressWarnings({"serial" , "javadoc"})
public class DebugConsole extends JFrame implements ActionListener {
	private static JTextArea console = new JTextArea();
	
	/** Use this method to print messages to the DebugConsole
	 *
	 * @param x
	 *            String contains the data to append to the console */
	protected static void print(String x) {
		console.append(x + "\n");
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	private Font buttonFont = new Font("Arial", Font.BOLD, 12);
	private JTextField consoleField = new JTextField();
	private ClientThread CT = null;
	private JButton menu = new JButton("Back");
	private JButton quit = new JButton("Close");
	private ServerThread ST = null;

	public DebugConsole(ServerThread s, ClientThread c) {
		this.ST = s;
		this.CT = c;
		this.setName("DebugConsole");
		this.setTitle("DebugConsole");
		this.getContentPane().setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(640, 320));
		console.setEditable(false);
		this.consoleField.setSize(640, 32);
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		// quit.addActionListener(this);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.menu) {
			if (this.CT != null && this.CT.isRunning())
				this.CT.stopThread();
			if (this.ST != null && this.ST.isRunning()) {
				this.ST.stopThread();;
			}
			this.dispose();
		} else if (e.getSource() == this.quit) {
			if (this.CT != null && this.CT.isRunning())
				this.CT.stopThread();
			if (this.ST != null && this.ST.isRunning())
				this.ST.stopThread();
			this.dispose();
		} else {
			// ???
		}
		console = null;
	}
}
