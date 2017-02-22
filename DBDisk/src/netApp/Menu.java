package netApp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** The main menu object
 * 
 * @author Gixbit */
@SuppressWarnings({"javadoc" , "serial"})
public class Menu extends JFrame implements ActionListener {
	ImageIcon logoImg = new ImageIcon(getClass().getResource(""));
	JButton HostGame = new JButton("Host Server");
	JButton FindGame = new JButton("Join Server");
	JButton quit = new JButton("Quit");
	JTextField IPaddress = new JTextField("Enter IP adress here!");
	JTextField userName = new JTextField("UnnamedUser");
	Font buttonFont = new Font("Arial", Font.BOLD, 30);
	Font IPFont = new Font("Arial", Font.BOLD, 20);
	
	public Menu() {
		JPanel top = new JPanel();
		userName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (userName.getText().equalsIgnoreCase("UnnamedUser")) {
					userName.setText("");
				} else {
					return;
				}
			}
		});
		userName.setFont(new Font("Arial", Font.BOLD, 18));
		getContentPane().setLayout(new GridLayout(2, 1, 10, 10));
		top.setLayout(new GridLayout(0, 1, 5, 5));
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(2, 2, 5, 5));
		IPaddress.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (IPaddress.getText().equalsIgnoreCase("Enter IP adress here!")) {
					IPaddress.setText("");
				} else {
					return;
				}
			}
		});
		top.add(IPaddress);
		IPaddress.setFont(IPFont);
		top.add(userName);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		HostGame.setPreferredSize(new Dimension(250, 50));
		HostGame.setFont(buttonFont);
		FindGame.setFont(buttonFont);
		quit.setFont(buttonFont);
		bottom.add(HostGame);
		bottom.add(FindGame);
		bottom.add(quit);
		getContentPane().add(top);
		getContentPane().add(bottom);
		HostGame.addActionListener(this);
		FindGame.addActionListener(this);
		quit.addActionListener(this);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == HostGame) {
			ClientThread CT = new ClientThread(5555, userName.getText().trim());
			CT.start();
			ServerThread ST = new ServerThread(CT);
			ST.start();
			new ChatConsole(CT);
			new DebugConsole(ST, CT);
			this.dispose();
		} else if (e.getSource() == FindGame) {
			ClientThread CT = new ClientThread(IPaddress.getText(), 5555, userName.getText().trim());
			CT.start();
			new ChatConsole(CT);
			this.dispose();
		} else if (e.getSource() == quit) {
			this.dispose();
		} else {
			throw new RuntimeException("Bad Click");
		}
	}
}
