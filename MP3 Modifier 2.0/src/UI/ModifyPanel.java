package UI;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
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
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Main.MP3FileSet;

public class ModifyPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static MP3FileSet mp3;
	private static ArrayList<MP3FileSet> playlist = new ArrayList<MP3FileSet>();
	private static int index = 0;
	
	private static JPanel modPanel;
	
	private static File cover;
	private static JLabel coverArt;
	private static JTextField txtAlbum, txtArtist, txtComment, txtGenre, txtTitle;
	private static JTextArea txtrLyrics;
	
	private String picPath, filePath;

	private DropTargetListener picDropListener;
	private DropTargetListener mp3DropListener;
	
	private static JButton btnSelectPic, btnSelectFile;

	public ModifyPanel(JPanel mainPanel, JFrame frame)
	{
		this.setOpaque(false);
		this.setBounds(252, 23, 523, 70);
		mainPanel.add(this);
		this.setLayout(null);
		
		// File Explorers
		String userDir = System.getProperty("user.home"); // Find the user Folder
		FileDialog chooserFile = new FileDialog(frame, "Choose a Song", FileDialog.LOAD);
		chooserFile.setDirectory(userDir + "\\Desktop");
		chooserFile.setFile("*.mp3");
		chooserFile.setVisible(false);
		
		FileDialog chooserPic = new FileDialog(frame, "Select a Picture", FileDialog.LOAD);
		chooserPic.setDirectory(userDir + "\\Desktop");
		chooserPic.setFile("*.jpg;*.jpeg;*.png");
		chooserPic.setVisible(false);
		
		mp3DragDrop();
		picDragDrop();
		
		modPanel = new JPanel();
		modPanel.setOpaque(false);
		modPanel.setBounds(252, 104, 523, 418);
		mainPanel.add(modPanel);
		modPanel.setLayout(null);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);
		btnPanel.setBounds(279, 330, 215, 79);
		modPanel.add(btnPanel);
		btnPanel.setLayout(null);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(0, 0, 99, 38);
		btnPanel.add(btnSave);
		btnSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(filePath != null)
				{
					String savePath = filePath;					
					
					if(chooserFile.getFile() != null)
						save(filePath, savePath);
					
					chooserFile.setFile("*.mp3");
					chooserPic.setFile("*.jpg;*.jpeg;*.png");
				}
			}
		}
		);
		btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		btnSave.setFocusPainted(false);
		
		JButton btnCancel = new JButton("Reset");
		btnCancel.setBounds(58, 83, 99, 38);
		btnPanel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(mp3 != null)
					updateInfo();
			}
		}
		);
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		btnCancel.setFocusPainted(false);
		
		JButton btnNext = new JButton("Next");
		btnNext.setToolTipText("Move to the Next Song, if multiple songs were loaded");
		btnNext.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				index++;
				if(index > playlist.size() - 1)
					index = 0;
				
				mp3 = playlist.get(index);
				filePath = mp3.getFile().getPath();
				btnSelectFile.setText(filePath);
				
				picPath = "Select a Pic";
				btnSelectPic.setText(picPath);
				cover = null;
				
				updateInfo();
			}
		}
		);
		btnNext.setBounds(116, 41, 99, 38);
		btnPanel.add(btnNext);
		btnNext.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnNext.setFocusPainted(false);
		
		JButton btnPrev = new JButton("Previous");
		btnPrev.setToolTipText("Move to the Previous Song, if multiple songs were loaded");
		btnPrev.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				index--;
				if(index < 0)
					index = playlist.size() - 1;
				
				mp3 = playlist.get(index);
				filePath = mp3.getFile().getPath();
				btnSelectFile.setText(filePath);
				
				picPath = "Select a Pic";
				btnSelectPic.setText(picPath);
				cover = null;
				
				updateInfo();
			}
		}
		);
		btnPrev.setBounds(0, 41, 99, 38);
		btnPanel.add(btnPrev);
		btnPrev.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnPrev.setFocusPainted(false);
		
		JButton btnSaveAs = new JButton("Save As");
		btnSaveAs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(filePath != null)
				{		
					chooserFile.setVisible(true);
					String savePath = chooserFile.getDirectory() + chooserFile.getFile();					
					
					if(chooserFile.getFile() != null)
						save(filePath, savePath);
					
					chooserFile.setFile("*.mp3");
					chooserPic.setFile("*.jpg;*.jpeg;*.png");
				}
			}
		}
		);
		btnSaveAs.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnSaveAs.setFocusPainted(false);
		btnSaveAs.setBounds(116, 0, 99, 38);
		btnPanel.add(btnSaveAs);
		
		coverArt = new JLabel("");
		coverArt.setBounds(0, 0, 284, 284);
		modPanel.add(coverArt);
		Image noImg = new ImageIcon(this.getClass().getResource("/Resources/Images/No Image.jpg")).getImage();
		coverArt.setIcon(new ImageIcon(noImg));
		
		txtComment = new JTextField();
		txtComment.setBounds(348, 299, 175, 24);
		modPanel.add(txtComment);
		txtComment.setText("Blank");
		txtComment.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtComment.setColumns(10);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setBounds(249, 295, 94, 32);
		modPanel.add(lblComment);
		lblComment.setHorizontalAlignment(SwingConstants.RIGHT);
		lblComment.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		txtGenre = new JTextField();
		txtGenre.setBounds(76, 390, 175, 24);
		modPanel.add(txtGenre);
		txtGenre.setText("Blank");
		txtGenre.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtGenre.setColumns(10);
		
		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setBounds(10, 386, 62, 32);
		modPanel.add(lblGenre);
		lblGenre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGenre.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		txtAlbum = new JTextField();
		txtAlbum.setBounds(75, 358, 175, 24);
		modPanel.add(txtAlbum);
		txtAlbum.setText("Blank");
		txtAlbum.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtAlbum.setColumns(10);
		
		JLabel lblAlbum = new JLabel("Album:");
		lblAlbum.setBounds(10, 354, 62, 32);
		modPanel.add(lblAlbum);
		lblAlbum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAlbum.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		txtArtist = new JTextField();
		txtArtist.setBounds(75, 328, 175, 24);
		modPanel.add(txtArtist);
		txtArtist.setText("Blank");
		txtArtist.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtArtist.setColumns(10);
		
		JLabel lblArtist = new JLabel("Artist:");
		lblArtist.setBounds(18, 324, 53, 32);
		modPanel.add(lblArtist);
		lblArtist.setHorizontalAlignment(SwingConstants.RIGHT);
		lblArtist.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		txtTitle = new JTextField();
		txtTitle.setBounds(75, 299, 175, 24);
		modPanel.add(txtTitle);
		txtTitle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtTitle.setText("Blank");
		txtTitle.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(19, 295, 51, 32);
		modPanel.add(lblTitle);
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		JScrollPane scrollLyricsPane = new JScrollPane();
		scrollLyricsPane.setBounds(308, 30, 215, 254);
		modPanel.add(scrollLyricsPane);
		
		txtrLyrics = new JTextArea();
		txtrLyrics.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txtrLyrics.setText("Blank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\nBlank\n");
		scrollLyricsPane.setViewportView(txtrLyrics);
		
		JLabel lblLyrics = new JLabel("Lyrics:");
		lblLyrics.setHorizontalAlignment(SwingConstants.CENTER);
		lblLyrics.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblLyrics.setBounds(381, 0, 70, 32);
		modPanel.add(lblLyrics);
		
		btnSelectFile = new JButton("Select a File");
		btnSelectFile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				chooserFile.setVisible(true);
				filePath = chooserFile.getDirectory() + chooserFile.getFile();

				if(chooserFile.getFile() != null)
				{
					mp3 = new MP3FileSet(new File(filePath));
					
					picPath = "Select a Pic";
					btnSelectPic.setText(picPath);
					
					updateInfo();
					btnSelectFile.setText(filePath);
					chooserFile.setFile("*.mp3");
					
					playlist.add(mp3);
				}
			}
		}
		);
		btnSelectFile.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFile.setFocusPainted(false);
		btnSelectFile.setBounds(71, 0, 452, 30);
		btnSelectFile.setDropTarget(new DropTarget(btnSelectFile, mp3DropListener));
		this.add(btnSelectFile);
		
		JLabel lblFile = new JLabel("File:");
		lblFile.setForeground(Color.BLACK);
		lblFile.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblFile.setBounds(0, 0, 65, 30);
		this.add(lblFile);
		
		btnSelectPic = new JButton("Select a Picture");
		btnSelectPic.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				chooserPic.setVisible(true);
				picPath = chooserPic.getDirectory() + chooserPic.getFile();

				if(chooserPic.getFile() != null)
				{
					cover = new File(picPath);
					if(cover.exists())
					{
						Image pic = null, resized2 = null;
						try 
						{
							pic = ImageIO.read(cover);
							resized2 = pic.getScaledInstance(284, 284, Image.SCALE_DEFAULT);
						} 
						catch (Exception e1) 
						{
							e1.printStackTrace();
						}
						
						coverArt.setIcon(new ImageIcon(resized2));
						
						btnSelectPic.setText(picPath);
						chooserPic.setFile("*.jpg;*.jpeg;*.png");
					}
				}
			}
		}
		);
		btnSelectPic.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectPic.setFocusPainted(false);
		btnSelectPic.setBounds(71, 40, 452, 30);
		btnSelectPic.setDropTarget(new DropTarget(btnSelectPic, picDropListener));
		this.add(btnSelectPic);
		
		JLabel lblPic = new JLabel("Picture:");
		lblPic.setForeground(Color.BLACK);
		lblPic.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPic.setBounds(0, 40, 65, 30);
		this.add(lblPic);

		modPanel.setVisible(true);
	}
	
	private void updateInfo()
	{
		if(mp3.getCover() != null)
		{
			Image resized = mp3.getCover().getScaledInstance(284, 284, Image.SCALE_DEFAULT);
			coverArt.setIcon(new ImageIcon(resized));
		}
		
		else
		{
			Image noImg = new ImageIcon(this.getClass().getResource("/Resources/Images/No Image.jpg")).getImage();
			coverArt.setIcon(new ImageIcon(noImg));
		}
		
		txtTitle.setText(mp3.getTitle());
		txtArtist.setText(mp3.getArtist());
		txtGenre.setText(mp3.getGenre());
		txtAlbum.setText(mp3.getAlbum());
		txtComment.setText(mp3.getComment());
		txtrLyrics.setText(mp3.getLyrics());
	}
	
	public void resetInfo()
	{
		if(mp3 != null)
		{
			mp3.stop();
			mp3 = null;
		}
		
		playlist = new ArrayList<MP3FileSet>();
		
		Image noImg = new ImageIcon(this.getClass().getResource("/Resources/Images/No Image.jpg")).getImage();
		coverArt.setIcon(new ImageIcon(noImg));
		cover = null;
		txtTitle.setText("");
		txtArtist.setText("");
		txtGenre.setText("");
		txtAlbum.setText("");
		txtComment.setText("");
		txtrLyrics.setText("");
		
		btnSelectFile.setText("Select a Folder");
		btnSelectPic.setText("Select a Picture");
	}
	
	private static void save(String filePath, String savePath)
	{
		// Create the Copy
		try 
		{
			Files.copy(new File(filePath).toPath(), new File(savePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} 
		
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
		
		File updatedFile = new File(savePath);
		MP3FileSet mp3Set = new MP3FileSet(updatedFile);
		
		// Update Data
		if(cover != null)
			mp3Set.setCover(cover);
		
		mp3Set.setAlbum(txtAlbum.getText());
		mp3Set.setArtist(txtArtist.getText());
		mp3Set.setComment(txtComment.getText());
		mp3Set.setGenre(txtGenre.getText());
		mp3Set.setLyrics(txtrLyrics.getText());
		mp3Set.setTitle(txtTitle.getText());
		
		try 
		{
			mp3Set.getAudioFile().save();
		} 
		
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
	}
	
	public void showModPanel()
	{
		modPanel.setVisible(true);
	}
	
	public void hideModPanel()
	{
		modPanel.setVisible(false);
	}
	
	private void mp3DragDrop()
	{
		// 0 for mp3 file, 1 for picture
		
		mp3DropListener = new DropTargetListener() 
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

							for(File file:files)
							{
								if(file.isDirectory())
								{
									File[] fileList = file.listFiles();

									for(File dirFile:fileList)
									{
										if(checkExtension(dirFile, 0) && dirFile.isFile()) // mp3
										{
											mp3 = new MP3FileSet(dirFile);
											filePath = dirFile.getPath();
											updateInfo();
											btnSelectFile.setText(filePath);

											playlist.add(mp3);
										}
									}
								}

								else if(checkExtension(file, 0) && file.isFile()) // mp3
								{
									mp3 = new MP3FileSet(file);
									filePath = file.getPath();
									updateInfo();
									btnSelectFile.setText(filePath);

									playlist.add(mp3);
								}
							}
							
							mp3 = playlist.get(0);

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
	
	private void picDragDrop()
	{
		// 0 for mp3 file, 1 for picture
		
		picDropListener = new DropTargetListener() 
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
							
							if(checkExtension(files.get(0), 1) && files.get(0).isFile())
							{
								picPath = files.get(0).getPath();
								cover = files.get(0);
								
								if(cover.exists())
								{
									Image pic = null, resized = null;
									try 
									{
										pic = ImageIO.read(cover);
										resized = pic.getScaledInstance(284, 284, Image.SCALE_DEFAULT);
									} 
									catch (Exception e1) 
									{
										e1.printStackTrace();
									}
									
									coverArt.setIcon(new ImageIcon(resized));
									btnSelectPic.setText(picPath);
								}
							}
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
	
	private static boolean checkExtension(File file, int mode)
	{
		// 0 for mp3, 1 for pictures
		if(mode == 0)
			return file.getName().substring(file.getName().lastIndexOf(".")).compareTo(".mp3") == 0;
		
		else
			return file.getName().substring(file.getName().lastIndexOf(".")).compareTo(".png") == 0 ||
				   file.getName().substring(file.getName().lastIndexOf(".")).compareTo(".jpg") == 0 || 
				   file.getName().substring(file.getName().lastIndexOf(".")).compareTo(".jpeg") == 0;
	}
}
