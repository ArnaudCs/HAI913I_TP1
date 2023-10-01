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
import java.util.ArrayList;
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
import javax.swing.JSeparator;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;


public class CodeAnalyserGUI {

	private JFrame frmCodeanaliser;
	private static final int PADDING = 3;   // for example
	private String filePath;
	private File folder;

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
		frmCodeanaliser.setMinimumSize(new Dimension(500, 300));
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBorder(new LineBorder(new Color(31, 110, 140), 14));
		panelHeader.setBackground(new Color(31, 110, 140));
		frmCodeanaliser.getContentPane().add(panelHeader, BorderLayout.NORTH);
		
		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(new Color(31, 110, 140));
		frmCodeanaliser.getContentPane().add(panelFooter, BorderLayout.SOUTH);
		panelFooter.setLayout(new BorderLayout(0, 0));
		
		final JTextArea cmdDisplayPanel = new JTextArea(9, 30);
		cmdDisplayPanel.setLineWrap(true);
		cmdDisplayPanel.setWrapStyleWord(true);
		cmdDisplayPanel.setFont(new Font("Monospaced", Font.ITALIC, 16));
		cmdDisplayPanel.setForeground(new Color(255, 255, 255));
		cmdDisplayPanel.setBackground(new Color(14, 41, 84));
		
		JScrollPane ScrollableCmd = new JScrollPane(
				cmdDisplayPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		panelFooter.add(ScrollableCmd);
		
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
		panelHeader.setLayout(new MigLayout("", "[182px]", "[29px]"));
		
		JLabel header = new JLabel("Code Analyser");
		header.setForeground(new Color(255, 255, 255));
		header.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 28));
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
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		panel_4.add(lblNewLabel);

		
		final JLabel choosedFilePath = new JLabel("Chosen File Path");
		choosedFilePath.setForeground(new Color(255, 255, 255));
		choosedFilePath.setFont(new Font("Tahoma", Font.BOLD, 13));
		choosedFilePath.setVisible(false);
		panel_4.add(choosedFilePath);
		
		final JButton chooseProjectBtn = new JButton("Choose Directory");
		chooseProjectBtn.setForeground(new Color(255, 255, 255));
		chooseProjectBtn.setBackground(new Color(132, 167, 161));
		panel_4.add(chooseProjectBtn);
		
		final JButton discardChoosedProject = new JButton("Discard");
		discardChoosedProject.setBackground(new Color(255, 128, 128));
		discardChoosedProject.setForeground(new Color(255, 255, 255));
		discardChoosedProject.setVisible(false);
		panel_4.add(discardChoosedProject);
		
		final JButton analyseBtn = new JButton("Analyse");
		analyseBtn.setBackground(new Color(0, 255, 0));
		analyseBtn.setForeground(new Color(255, 255, 255));
		analyseBtn.setToolTipText("Start process");
		analyseBtn.setVisible(false);
		panel_4.add(analyseBtn);
		
		//Choosing the filePath
		chooseProjectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(frmCodeanaliser, "Selected File : " + filePath);
                    choosedFilePath.setText("...." + filePath.substring(Math.max(0, filePath.length() - 20)));
                    
                    //Creating the folder with the source Path
                    folder = new File(filePath);
            		
                    choosedFilePath.setVisible(true);
                    chooseProjectBtn.setVisible(false);
                    discardChoosedProject.setVisible(true);
            		analyseBtn.setEnabled(true);
            		analyseBtn.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frmCodeanaliser, "No file selected.");
                }
            }
        });
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(14, 41, 84));
		centerPanel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel separatorPanel = new JPanel();
		separatorPanel.setBackground(new Color(14, 41, 84));
		panel_6.add(separatorPanel, BorderLayout.NORTH);
		separatorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("- • • • • • -");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		separatorPanel.add(lblNewLabel_1);
		
		final JPanel mainContentPanel = new JPanel();
		panel_6.add(mainContentPanel, BorderLayout.CENTER);
		mainContentPanel.setLayout(new BorderLayout(0, 0));
		
		final JPanel subMainContentPanel = new JPanel();
		subMainContentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		final JPanel subMainContentPanelWaiting = new JPanel();
		mainContentPanel.add(subMainContentPanelWaiting, BorderLayout.CENTER);
		subMainContentPanelWaiting.setBackground(new Color(14, 41, 84));
		subMainContentPanelWaiting.setVisible(true);
		subMainContentPanelWaiting.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		final JLabel waitingText = new JLabel("Click on analyse to see your project informations");
		waitingText.setForeground(new Color(255, 255, 255));
		waitingText.setFont(new Font("Tahoma", Font.BOLD, 25));
		subMainContentPanelWaiting.add(waitingText);
		
		//Class count panel
		
		JPanel classCountPanel = new JPanel();
		classCountPanel.setBackground(new Color(14, 41, 84));
		subMainContentPanel.add(classCountPanel);
		
		final JLabel classCountNumber = new JLabel("?");
		classCountNumber.setForeground(new Color(255, 255, 255));
		classCountNumber.setFont(new Font("Tahoma", Font.BOLD, 25));
		classCountPanel.add(classCountNumber);
		
		JLabel classCountLabel = new JLabel("Classe(s)");
		classCountLabel.setForeground(new Color(255, 255, 255));
		classCountLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		classCountPanel.add(classCountLabel);
		
		//Line count panel
		
		JPanel totalCountLinesPanel = new JPanel();
		totalCountLinesPanel.setBackground(new Color(14, 41, 84));
		subMainContentPanel.add(totalCountLinesPanel);
		
		final JLabel totalLinesNumber = new JLabel("?");
		totalLinesNumber.setForeground(new Color(255, 255, 255));
		totalLinesNumber.setFont(new Font("Tahoma", Font.BOLD, 25));
		totalCountLinesPanel.add(totalLinesNumber);
		
		JLabel lineCountLabel = new JLabel("Line(s) of code");
		lineCountLabel.setForeground(new Color(255, 255, 255));
		lineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		totalCountLinesPanel.add(lineCountLabel);
		
		//Method count panel
		
		JPanel totalMethodPanel = new JPanel();
		totalMethodPanel.setBackground(new Color(14, 41, 84));
		subMainContentPanel.add(totalMethodPanel);
		
		final JLabel methodCountNumber = new JLabel("?");
		methodCountNumber.setForeground(new Color(255, 255, 255));
		methodCountNumber.setFont(new Font("Tahoma", Font.BOLD, 25));
		totalMethodPanel.add(methodCountNumber);
		
		JLabel methodCountLabel = new JLabel("Method(s)");
		methodCountLabel.setForeground(new Color(255, 255, 255));
		methodCountLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		totalMethodPanel.add(methodCountLabel);
		
		//Packages count panel
		
		JPanel packageCountPanel = new JPanel();
		packageCountPanel.setBackground(new Color(14, 41, 84));
		subMainContentPanel.add(packageCountPanel);
		
		final JLabel packageCountNumber = new JLabel("?");
		packageCountNumber.setForeground(new Color(255, 255, 255));
		packageCountNumber.setFont(new Font("Tahoma", Font.BOLD, 25));
		packageCountPanel.add(packageCountNumber);
		
		JLabel packageCountLabel = new JLabel("Package(s)");
		packageCountLabel.setForeground(new Color(255, 255, 255));
		packageCountLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		packageCountPanel.add(packageCountLabel);
		
	    
		frmCodeanaliser.setBackground(new Color(255, 255, 255));
		frmCodeanaliser.setBounds(100, 100, 1200, 700);
		
		//Anlayse button action
		analyseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdDisplayPanel.setText("Code analysis startup");
				CodeAnalyser analyse = new CodeAnalyser();
				CodeAnalyser.runAllStats(folder);
				totalLinesNumber.setText(Integer.toString(analyse.getProjectLinesOfCode()));
				methodCountNumber.setText(Integer.toString(analyse.getProjectMethodsNumber()));
				packageCountNumber.setText(Integer.toString(analyse.getProjectPackagesNumber()));
				classCountNumber.setText(Integer.toString(analyse.getProjectClassesNumber()));
				cmdDisplayPanel.setText(analyse.getCmd());
				subMainContentPanelWaiting.setVisible(false);
				mainContentPanel.add(subMainContentPanel, BorderLayout.CENTER);
			}
		});
		
		
		//Choosing another project path
		discardChoosedProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choosedFilePath.setText("");
				filePath = null;
				discardChoosedProject.setVisible(false);
				chooseProjectBtn.setVisible(true);
				analyseBtn.setEnabled(false);
				analyseBtn.setVisible(false);
				cmdDisplayPanel.setText("");
				subMainContentPanelWaiting.setVisible(true);
				mainContentPanel.remove(subMainContentPanel);
			}
		});
				
		//background
	    //ImageIcon background = new ImageIcon("/home/e20190000683/Bureau/HAI913I_TP1/Background.png");
	    ImageIcon background = new ImageIcon("C:\\Users\\arnau\\Desktop\\HAI913I_TP1\\Background.png");
	    Image scaledImgBackground = background.getImage().getScaledInstance((int) frmCodeanaliser.getBounds().getWidth(), (int) frmCodeanaliser.getBounds().getHeight(), Image.SCALE_SMOOTH);
	    ImageIcon scaledBackgroundImage = new ImageIcon(scaledImgBackground);
	    
		frmCodeanaliser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
