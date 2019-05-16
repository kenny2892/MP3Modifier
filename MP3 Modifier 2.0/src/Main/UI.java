package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import UI.*;

public class UI 
{
	private static MenuManager manager;
	
	// Main Menu
	private JFrame frame;
	private JPanel mainPanel;

	private static IntroPanel introPanel;
	private static PlayPanel playPanel;
	private static ModifyPanel modifyPanel;
	private static NameChange namePanel;
	private static AddTitles addTitles;
	
	private static MP3FileSet mp3;
	
	public UI()
	{
		mainMenu();
	}
	
	public void addManager(MenuManager manager2)
	{
		manager = manager2;
	}
	
	private void mainMenu()
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) 
		{
			System.out.println("Shit");
		}
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(UI.class.getResource("/Resources/Images/Icon.png")));
		frame.setTitle("MP3 Modifier");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 794, 571);
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		playPanel = new PlayPanel();
		playPanel.showInfo(mainPanel);
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setBorder(new LineBorder(new Color(0, 0, 0), 5, true));
		actionsPanel.setBounds(10, 250, 220, 277);
		mainPanel.add(actionsPanel);
		actionsPanel.setLayout(null);
		
		JList<String> lstActions = new JList<String>();
		lstActions.setBounds(10, 50, 200, 173);
		actionsPanel.add(lstActions);
		lstActions.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lstActions.setBorder(new LineBorder(new Color(0, 0, 0)));
		lstActions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstActions.setModel(new AbstractListModel<String>() 
		{
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {" Play", " Modify File", " Change Names", " Add Titles"};
			public int getSize() 
			{
				return values.length;
			}
			public String getElementAt(int index) 
			{
				return values[index];
			}
		}
		);
		
		JLabel lblActions = new JLabel("Actions:");
		lblActions.setBounds(56, 11, 104, 39);
		actionsPanel.add(lblActions);
		lblActions.setHorizontalAlignment(SwingConstants.CENTER);
		lblActions.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		
		JButton btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int result = lstActions.getSelectedIndex();
				actionList(result);
			}
		}
		);
		btnGo.setFocusPainted(false);
		btnGo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnGo.setBounds(60, 229, 92, 38);
		actionsPanel.add(btnGo);

		modifyPanel = new ModifyPanel(mainPanel, frame);
		introPanel = new IntroPanel(mainPanel);
		playPanel.createPlayPanel(mainPanel, mp3);
		namePanel = new NameChange(mainPanel);
		addTitles = new AddTitles(mainPanel);
		
		JLabel bg = new JLabel("");
		bg.setBounds(0, 0, 794, 571);
		Image bgImg = new ImageIcon(this.getClass().getResource("/Resources/Images/music_bg.jpg")).getImage();
		bg.setIcon(new ImageIcon(bgImg));
		mainPanel.add(bg);
		
		frame.setVisible(true);
	}
	
	private static void actionList(int num)
	{
		switch(num)
		{
		case 0:
			manager.showPlay();
			break;
		
		case 1:
			manager.showModify();
			break;

		case 2:
			manager.showNameChange();
			break;

		case 3:
			manager.showAddTitles();
			break;
		}
	}
	
	public IntroPanel getIntro()
	{
		return introPanel;
	}
	
	public PlayPanel getPlay()
	{
		return playPanel;
	}
	
	public ModifyPanel getModify()
	{
		return modifyPanel;
	}
	
	public NameChange getNameChange()
	{
		return namePanel;
	}
	
	public AddTitles getAddTitles()
	{
		return addTitles;
	}
}
