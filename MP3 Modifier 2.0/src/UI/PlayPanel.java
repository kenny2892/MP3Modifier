package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import Main.MP3FileSet;
import Main.UI;

public class PlayPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static JProgressBar progressBar;
	private static JLabel lblTitle;
	private static JToggleButton btnPlay;
	private static JButton btnSelectFolder;
	private DropTargetListener dirDropListener;
	
	private MP3FileSet mp3;
	private static ArrayList<MP3FileSet> playlist;	

	private static JPanel infoPanel;
	private static JTextArea txtrInfoLyrics;
	private static JLabel lblTitleFill, lblGenreFill, lblAlbumFill, lblArtistFill ,lblCommentFill, coverArt2;
	private static JSlider volumeSlider;
	
	private static String totalStr = "00:00", currentStr = "00:00";;
	private static int currentTime = 0, totalTime = 0;
	private static Timer timer;
	
	private static boolean unpause = false;
	private static int volume, songIndex;
	private String filePath = "";
	
	public void createPlayPanel(JPanel mainPanel, MP3FileSet newMp3)
	{
		this.mp3 = newMp3;
		
		songIndex = 0;
		volume = 50;
		
		this.setOpaque(false);
		this.setBounds(253, 23, 521, 527);
		mainPanel.add(this);
		this.setLayout(null);
		
		JFileChooser chooserDir = new JFileChooser();
		String userDir = System.getProperty("user.home"); // Find the user Folder
		chooserDir.setCurrentDirectory(new File(userDir + "/Desktop")); // Set to User's Desktop
		chooserDir.setDialogTitle("Select Folder"); // GUI Title
		chooserDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JPanel timelinePanel = new JPanel();
		timelinePanel.setOpaque(false);
		timelinePanel.setBounds(0, 363, 522, 106);
		this.add(timelinePanel);
		timelinePanel.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(0, 28, 522, 20);
		timelinePanel.add(progressBar);
		progressBar.setBorder(new LineBorder(new Color(255, 255, 255), 3));
		progressBar.setForeground(new Color(30, 144, 255));
		progressBar.setString("");
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		
		JLabel lblTime = new JLabel("Time: " + currentStr + "/" + totalStr);
		lblTime.setBounds(120, 0, 280, 30);
		timelinePanel.add(lblTime);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTime.setBackground(new Color(255, 255, 255));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		
		dirDragDrop(lblTime);
		
		if(mp3 != null)
		{
			currentStr = mp3.getCurrentTime();
			totalStr = mp3.getTotalTime();
			
			currentTime = mp3.getCurrentTimeInt();
			totalTime = mp3.getLength();
		}
		
		timer = new Timer(1000, new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(currentTime < totalTime)
					currentTime++;
				
				else if(currentTime == totalTime && playlist != null) // Next Song
				{
					songIndex++;
					if(songIndex > playlist.size() - 1)
						songIndex = 0;

					mp3 = playlist.get(songIndex);
					mp3.setVolume(volume);
					mp3.play();
					currentTime = 0;
				}

				if(mp3 != null)
				{
					mp3.setCurrentTime(currentTime);
					currentStr = mp3.getCurrentTime();
					totalStr = mp3.getTotalTime();

					lblTime.setText("Time: " + currentStr + " / " + totalStr);

					progressBar.setMaximum(totalTime);
					progressBar.setValue(currentTime);

					updateInfo();
				}
			}
		}
		);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(120, 46, 280, 60);
		buttonPanel.setOpaque(false);
		timelinePanel.add(buttonPanel);
		buttonPanel.setBackground(new Color(255, 255, 255));
		buttonPanel.setLayout(new GridLayout(0, 4, 10, 0));
		
		Image sl = new ImageIcon(this.getClass().getResource("/Resources/Images/Skip Left.png")).getImage();
		Image play = new ImageIcon(this.getClass().getResource("/Resources/Images/Play.png")).getImage();
		Image stop = new ImageIcon(this.getClass().getResource("/Resources/Images/Stop.png")).getImage();
		Image sr = new ImageIcon(this.getClass().getResource("/Resources/Images/Skip Right.png")).getImage();
		
		JButton btnSL = new JButton(""); // Have to put this up here so it is in the right order
		buttonPanel.add(btnSL);
		
		btnPlay = new JToggleButton("");
		btnPlay.setSelectedIcon(new ImageIcon(UI.class.getResource("/Resources/Images/Pause.png")));
		btnPlay.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{				
				if(unpause == false)
				{
					unpause = true;
					timer.start();
					if(mp3 != null)
						mp3.play();
				}
				
				else
				{
					unpause = false;
					timer.stop();
					if(mp3 != null)
						mp3.pause();
				}
			}
		}
		);
		btnPlay.setFocusPainted(false);
		btnPlay.setContentAreaFilled(false);
		buttonPanel.add(btnPlay);
		btnPlay.setIcon(new ImageIcon(play));
		
		JButton btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				currentTime = 0;
				lblTime.setText("Time: 00:00 / " + totalStr);
				timer.stop();
				
				progressBar.setValue(0);
				
				if(mp3 != null)
					mp3.stop();
				
				if(unpause == true)
				{
					btnPlay.doClick();
					mp3.stop();
				}
			}
		}
		);
		btnStop.setContentAreaFilled(false);
		btnStop.setFocusPainted(false);
		buttonPanel.add(btnStop);
		btnStop.setIcon(new ImageIcon(stop));
		
		btnSL.setContentAreaFilled(false);
		btnSL.setFocusPainted(false);
		btnSL.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				mp3.stop();
				songIndex--;
				if(songIndex == -1)
					songIndex = playlist.size() - 1;
				
				mp3 = playlist.get(songIndex);
				mp3.setVolume(volume);
				currentTime = 0;
				
				currentStr = mp3.getCurrentTime();
				totalStr = mp3.getTotalTime();
				
				lblTime.setText("Time: " + currentStr + " / " + totalStr);
				
				progressBar.setMaximum(totalTime);
				progressBar.setValue(currentTime);
				
				updateInfo();
				timer.start();
				
				btnPlay.doClick();
				
				if(unpause == false)
				{
					btnPlay.doClick();
					mp3.play();
				}
			}
		}
		);
		btnSL.setIcon(new ImageIcon(sl));
		
		JButton btnSR = new JButton("");
		btnSR.setContentAreaFilled(false);
		btnSR.setFocusPainted(false);
		btnSR.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				mp3.stop();
				songIndex++;
				if(songIndex > playlist.size() - 1)
					songIndex = 0;
				
				mp3 = playlist.get(songIndex);
				mp3.setVolume(volume);
				currentTime = 0;
				
				currentStr = mp3.getCurrentTime();
				totalStr = mp3.getTotalTime();
				
				lblTime.setText("Time: " + currentStr + " / " + totalStr);
				
				progressBar.setMaximum(totalTime);
				progressBar.setValue(currentTime);
				
				updateInfo();
				timer.start();
				
				btnPlay.doClick();
				
				if(unpause == false)
				{
					btnPlay.doClick();
					mp3.play();
				}
			}
		}
		);
		buttonPanel.add(btnSR);
		btnSR.setIcon(new ImageIcon(sr));
		
		coverArt2 = new JLabel("");
		coverArt2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		coverArt2.setBounds(120, 45, 284, 284);
		this.add(coverArt2);
		Image noImg = new ImageIcon(this.getClass().getResource("/Resources/Images/No Image.jpg")).getImage();
		coverArt2.setIcon(new ImageIcon(noImg));
		
		JPanel playSelectPanel = new JPanel();
		playSelectPanel.setBounds(0, 0, 523, 37);
		this.add(playSelectPanel);
		playSelectPanel.setOpaque(false);
		playSelectPanel.setLayout(null);
		
		btnSelectFolder = new JButton("Select a Folder");
		btnSelectFolder.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(unpause == true)
					btnPlay.doClick();
				
				int result = chooserDir.showOpenDialog(btnSelectFolder);

				if(result == JFileChooser.APPROVE_OPTION)
				{
					if(chooserDir.getSelectedFile() != null)
					{
						filePath = chooserDir.getSelectedFile().getPath();
					}

					if(filePath.compareTo("") != 0)
						btnSelectFolder.setText(filePath);
						
					playlist = new ArrayList<MP3FileSet>();
					
					File folderOfSongs = new File(filePath);
					File[] songsArray = folderOfSongs.listFiles();
					ArrayList<File> songsAra = new ArrayList<>();
					
					for(int i = 0; i < songsArray.length; i++)
					{
						if(songsArray[i].getName().contains(".mp3"))
						{
							playlist.add(new MP3FileSet(songsArray[i]));
							songsAra.add(songsArray[i]);
						}
					}
				
					currentTime = 0;
					
					if(mp3 != null)
						mp3.stop();
					
					mp3 = playlist.get(0);
					mp3.setVolume(volume);
					
					updateInfo();
					lblTime.setText("Time: " + currentStr + "/ " + totalStr);
					
					if(unpause == false)
						btnPlay.doClick();
					
					else
						mp3.play();
				}
				
				else
					btnPlay.doClick();
			}
		}
		);
		btnSelectFolder.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFolder.setFocusPainted(false);
		btnSelectFolder.setBounds(69, 0, 452, 30);
		btnSelectFolder.setDropTarget(new DropTarget(btnSelectFolder, dirDropListener));
		playSelectPanel.add(btnSelectFolder);
		
		JLabel lblFolder = new JLabel("Folder:");
		lblFolder.setForeground(Color.BLACK);
		lblFolder.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblFolder.setBounds(0, 0, 65, 30);
		playSelectPanel.add(lblFolder);
		
		JButton btnInfo = new JButton("More Info");
		btnInfo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				infoPanel.setVisible(true);;
			}
		}
		);
		btnInfo.setFocusPainted(false);
		btnInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnInfo.setBounds(212, 480, 105, 37);
		this.add(btnInfo);
		
		lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(120, 333, 284, 26);
		this.add(lblTitle);
		
		volumeSlider = new JSlider();
		volumeSlider.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent arg0) 
			{
				volume = volumeSlider.getValue();
				
				if(mp3 != null)
					mp3.setVolume(volume);
			}
		}
		);
		volumeSlider.setRequestFocusEnabled(false);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.setMajorTickSpacing(10);
		volumeSlider.setMinorTickSpacing(5);
		volumeSlider.setOpaque(false);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setOrientation(SwingConstants.VERTICAL);
		volumeSlider.setBounds(405, 45, 49, 284);
		this.add(volumeSlider);
	}
	
	public void showInfo(JPanel mainPanel)
	{
		infoPanel = new JPanel();
		infoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		infoPanel.setBounds(144, 147, 505, 277);
		mainPanel.add(infoPanel);
		infoPanel.setLayout(null);
		
		JLabel lblInfo = new JLabel("Info");
		lblInfo.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setBounds(159, 11, 187, 34);
		infoPanel.add(lblInfo);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(10, 43, 77, 34);
		infoPanel.add(lblTitle);
		
		JLabel lblArtist = new JLabel("Artist:");
		lblArtist.setHorizontalAlignment(SwingConstants.RIGHT);
		lblArtist.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblArtist.setBounds(10, 70, 77, 34);
		infoPanel.add(lblArtist);
		
		JLabel lblAlbum = new JLabel("Album:");
		lblAlbum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAlbum.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblAlbum.setBounds(10, 98, 77, 34);
		infoPanel.add(lblAlbum);
		
		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGenre.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblGenre.setBounds(10, 125, 77, 34);
		infoPanel.add(lblGenre);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setHorizontalAlignment(SwingConstants.RIGHT);
		lblComment.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblComment.setBounds(10, 151, 77, 34);
		infoPanel.add(lblComment);
		
		JLabel lblLyrics = new JLabel("Lyrics:");
		lblLyrics.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLyrics.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblLyrics.setBounds(234, 43, 77, 34);
		infoPanel.add(lblLyrics);
		
		JScrollPane lyricsScrollPane = new JScrollPane();
		lyricsScrollPane.setBounds(321, 53, 174, 213);
		infoPanel.add(lyricsScrollPane);
		
		txtrInfoLyrics = new JTextArea();
		txtrInfoLyrics.setEditable(false);
		txtrInfoLyrics.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtrInfoLyrics.setText("Blank");
		lyricsScrollPane.setViewportView(txtrInfoLyrics);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				infoPanel.setVisible(false);
			}
		}
		);
		btnOk.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		btnOk.setFocusPainted(false);
		btnOk.setBounds(200, 205, 104, 44);
		infoPanel.add(btnOk);
		
		lblTitleFill = new JLabel("Blank");
		lblTitleFill.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitleFill.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblTitleFill.setBounds(97, 43, 138, 34);
		infoPanel.add(lblTitleFill);
		
		lblGenreFill = new JLabel("Blank");
		lblGenreFill.setHorizontalAlignment(SwingConstants.LEFT);
		lblGenreFill.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblGenreFill.setBounds(97, 125, 138, 34);
		infoPanel.add(lblGenreFill);
		
		lblAlbumFill = new JLabel("Blank");
		lblAlbumFill.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlbumFill.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblAlbumFill.setBounds(97, 98, 205, 34);
		infoPanel.add(lblAlbumFill);
		
		lblArtistFill = new JLabel("Blank");
		lblArtistFill.setHorizontalAlignment(SwingConstants.LEFT);
		lblArtistFill.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblArtistFill.setBounds(97, 70, 205, 34);
		infoPanel.add(lblArtistFill);
		
		lblCommentFill = new JLabel("Blank");
		lblCommentFill.setHorizontalAlignment(SwingConstants.LEFT);
		lblCommentFill.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblCommentFill.setBounds(97, 151, 138, 34);
		infoPanel.add(lblCommentFill);
		
		infoPanel.setVisible(false);
	}
	
	public void hideInfo()
	{
		infoPanel.setVisible(false);
	}
	
	private void updateInfo()
	{
		Image resized2 = mp3.getCover().getScaledInstance(284, 284, Image.SCALE_DEFAULT);
		coverArt2.setIcon(new ImageIcon(resized2));
		lblTitle.setText(mp3.getTitle());
		
		totalStr = mp3.getTotalTime();
		totalTime = mp3.getLength();
		
		lblTitleFill.setText(mp3.getTitle());
		lblArtistFill.setText(mp3.getArtist());
		lblAlbumFill.setText(mp3.getAlbum());
		lblGenreFill.setText(mp3.getGenre());
		lblCommentFill.setText(mp3.getComment());
		txtrInfoLyrics.setText(mp3.getLyrics());
		
		progressBar.setMaximum(totalTime);
		progressBar.setValue(currentTime);
	}
	
	public void resetInfo()
	{
		if(mp3 != null)
		{
			if(unpause == true)
				btnPlay.doClick();
			
			mp3.stop();
			mp3 = null;
			
			playlist = null;
		}
		
		Image noImg = new ImageIcon(this.getClass().getResource("/Resources/Images/No Image.jpg")).getImage();
		coverArt2.setIcon(new ImageIcon(noImg));
		lblTitle.setText("Title");
		
		totalStr = "00:00";
		currentTime = 0;
		totalTime = 0;
		
		lblTitleFill.setText("Blank");
		lblArtistFill.setText("Blank");
		lblAlbumFill.setText("Blank");
		lblGenreFill.setText("Blank");
		lblCommentFill.setText("Blank");
		txtrInfoLyrics.setText("Blank");
		
		progressBar.setMaximum(totalTime);
		progressBar.setValue(currentTime);
		
		btnSelectFolder.setText("Select a Folder");
		volume = 50;
		volumeSlider.setValue(50);
	}
	
	private void dirDragDrop(JLabel lblTime)
	{
		dirDropListener = new DropTargetListener() 
		{
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) 
			{
				
			}
			
			@Override
			public void drop(DropTargetDropEvent dtde) 
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY); // Accept Files
				
				Transferable t = dtde.getTransferable(); // Get the Files
				
				DataFlavor[] dataFlavors = t.getTransferDataFlavors(); // Get the formats of the Files
				
				for(DataFlavor f:dataFlavors) // Load thru Flavors
				{
					try
					{
						if(f.isFlavorJavaFileListType())
						{
							@SuppressWarnings("unchecked")
							List<File> files = (List<File>) t.getTransferData(f);
							
							playlist = new ArrayList<MP3FileSet>();
							
							if(unpause == true)
								btnPlay.doClick();

							filePath = files.get(0).getParent();

							if(filePath.compareTo("") != 0)
								btnSelectFolder.setText(filePath);
							
							for(File file:files)
							{								
								playlist.add(new MP3FileSet(file));
							}
							
							currentTime = 0;
							
							if(mp3 != null)
								mp3.stop();
							
							mp3 = playlist.get(0);
							mp3.setVolume(volume);
							
							updateInfo();
							lblTime.setText("Time: " + currentStr + "/ " + totalStr);
							
							if(unpause == false)
								btnPlay.doClick();
							
							else
								mp3.play();
						}
					}
					
					catch(Exception e)
					{
						System.out.println("Fuck");
					}
				}
			}
			
			@Override
			public void dragOver(DropTargetDragEvent dtde) 
			{
				
			}
			
			@Override
			public void dragExit(DropTargetEvent dte) 
			{
				
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent dtde) 
			{
				
			}
		};
	}
}
