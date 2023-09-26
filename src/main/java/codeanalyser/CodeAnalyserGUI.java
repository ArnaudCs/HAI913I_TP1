package codeanalyser;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import javax.swing.SwingConstants;


public class CodeAnalyserGUI {

	private JFrame frmCodeanaliser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeAnalyserGUI window = new CodeAnalyserGUI();
					window.frmCodeanaliser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CodeAnalyserGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCodeanaliser = new JFrame();
		frmCodeanaliser.setResizable(false);
		frmCodeanaliser.setTitle("Code Analyser");
		frmCodeanaliser.getContentPane().setBackground(new Color(255, 255, 255));
		frmCodeanaliser.getContentPane().setLayout(null);
		
		ImageIcon icon = new ImageIcon("/home/e20190000683/Bureau/HAI913I_TP1/Search.png");
		JLabel searchIcon = new JLabel("");
		// Redimensionner l'image pour correspondre à la taille du JLabel
		searchIcon.setBounds(12, 529, 125, 125);
	    Image scaledImg = icon.getImage().getScaledInstance((int) searchIcon.getBounds().getWidth(), (int) searchIcon.getBounds().getHeight(), Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImg);
	    searchIcon.setIcon(scaledIcon);
	    frmCodeanaliser.getContentPane().add(searchIcon);
	    
	    ImageIcon iconStat = new ImageIcon("/home/e20190000683/Bureau/HAI913I_TP1/Stat.png");
		JLabel statIcon = new JLabel("");
		// Redimensionner l'image pour correspondre à la taille du JLabel
		statIcon.setBounds(1128, 12, 50, 50);
	    Image scaledStatImg = iconStat.getImage().getScaledInstance((int) statIcon.getBounds().getWidth(), (int) statIcon.getBounds().getHeight(), Image.SCALE_SMOOTH);
	    ImageIcon scaledStatIcon = new ImageIcon(scaledStatImg);
	    statIcon.setIcon(scaledStatIcon);
	    frmCodeanaliser.getContentPane().add(statIcon);
	    
		frmCodeanaliser.setBackground(new Color(255, 255, 255));
		frmCodeanaliser.setBounds(100, 100, 1200, 700);
				
		//background
	    ImageIcon background = new ImageIcon("/home/e20190000683/Bureau/HAI913I_TP1/Background.png");
		JLabel backgroundImage = new JLabel("");
		// Redimensionner l'image pour correspondre à la taille du JLabel
		backgroundImage.setBounds(0, 0, (int)frmCodeanaliser.getBounds().getWidth(), (int) frmCodeanaliser.getBounds().getHeight());
	    Image scaledImgBackground = background.getImage().getScaledInstance((int) frmCodeanaliser.getBounds().getWidth(), (int) frmCodeanaliser.getBounds().getHeight(), Image.SCALE_SMOOTH);
	    ImageIcon scaledBackgroundImage = new ImageIcon(scaledImgBackground);
	    backgroundImage.setIcon(scaledBackgroundImage);
	    frmCodeanaliser.getContentPane().add(backgroundImage);
	    
		frmCodeanaliser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
