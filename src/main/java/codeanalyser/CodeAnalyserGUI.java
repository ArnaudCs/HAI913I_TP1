package codeanalyser;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JFileChooser;


public class CodeAnalyserGUI {

	private JFrame frmCodeanaliser;
	private static final int PADDING = 3;   // for example

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
		frmCodeanaliser.setTitle("Code Analyser");
		frmCodeanaliser.getContentPane().setBackground(new Color(255, 255, 255));
		frmCodeanaliser.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelSide = new JPanel();
		panelSide.setBackground(new Color(31, 110, 140));
		frmCodeanaliser.getContentPane().add(panelSide, BorderLayout.WEST);
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBorder(new LineBorder(new Color(31, 110, 140), 14));
		panelHeader.setBackground(new Color(31, 110, 140));
		frmCodeanaliser.getContentPane().add(panelHeader, BorderLayout.NORTH);
		
		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(new Color(31, 110, 140));
		frmCodeanaliser.getContentPane().add(panelFooter, BorderLayout.SOUTH);
		panelFooter.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrClassNames = new JTextArea();
		txtrClassNames.setFont(new Font("Monospaced", Font.ITALIC, 16));
		txtrClassNames.setText("Class names ...");
		txtrClassNames.setForeground(new Color(255, 255, 255));
		txtrClassNames.setBackground(new Color(14, 41, 84));
		txtrClassNames.setPreferredSize(new Dimension(0, 200));
		txtrClassNames.setEditable(false);
		panelFooter.add(txtrClassNames);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(31, 110, 140));
		panelFooter.add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(31, 110, 140));
		panelFooter.add(panel_1, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(31, 110, 140));
		panelFooter.add(panel_2, BorderLayout.WEST);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(31, 110, 140));
		panelFooter.add(panel_3, BorderLayout.EAST);
		panelHeader.setLayout(new MigLayout("", "[182px][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]", "[29px]"));
		
		JLabel header = new JLabel("Code Analyser");
		header.setForeground(new Color(255, 255, 255));
		header.setFont(new Font("MS UI Gothic", Font.BOLD, 28));
		panelHeader.add(header, "cell 0 0,alignx center,aligny top");
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(new Color(14, 41, 84));
		frmCodeanaliser.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel_4.setBackground(new Color(14, 41, 84));
        panel_4.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPanel.add(panel_4, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Choose project path : ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		panel_4.add(lblNewLabel);

		
		final JLabel lblNewLabel_1 = new JLabel("Chosen File Path");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setVisible(false);
		panel_4.add(lblNewLabel_1);
		
		final JButton btnNewButton = new JButton("Choose Directory");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(132, 167, 161));
		btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(frmCodeanaliser, "Fichier sélectionné : " + filePath);
                    lblNewLabel_1.setText("...." + filePath.substring(Math.max(0, filePath.length() - 20)));
                    lblNewLabel_1.setVisible(true);
                    btnNewButton.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(frmCodeanaliser, "Aucun fichier sélectionné.");
                }
            }
        });
		panel_4.add(btnNewButton);
		
		JPanel panel_5 = new JPanel();
		centerPanel.add(panel_5, BorderLayout.SOUTH);
		
		JPanel panel_6 = new JPanel();
		centerPanel.add(panel_6, BorderLayout.CENTER);
	    
		frmCodeanaliser.setBackground(new Color(255, 255, 255));
		frmCodeanaliser.setBounds(100, 100, 1200, 700);
				
		//background
	    //ImageIcon background = new ImageIcon("/home/e20190000683/Bureau/HAI913I_TP1/Background.png");
	    ImageIcon background = new ImageIcon("C:\\Users\\arnau\\Desktop\\HAI913I_TP1\\Background.png");
	    Image scaledImgBackground = background.getImage().getScaledInstance((int) frmCodeanaliser.getBounds().getWidth(), (int) frmCodeanaliser.getBounds().getHeight(), Image.SCALE_SMOOTH);
	    ImageIcon scaledBackgroundImage = new ImageIcon(scaledImgBackground);
	    
		frmCodeanaliser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
