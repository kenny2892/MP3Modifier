package UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Main.MP3FileSet;
import Main.SaveFiles;

public class NameChange extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static ArrayList<MP3FileSet> playlist = new ArrayList<MP3FileSet>();
	private static ArrayList<File> songsAra;
	
	private static JSpinner spinnerTitle, spinnerArtist, spinnerAlbum;
	private static JButton btnSelectFolder;
	
	private DropTargetListener dirDropListener;
	
	public NameChange(JPanel mainPanel)
	{
		this.setOpaque(false);
		this.setBounds(242, 11, 542, 516);
		mainPanel.add(this);
		this.setLayout(null);
		
		JFileChooser chooserDir = new JFileChooser();
		String userDir = System.getProperty("user.home"); // Find the user Folder
		chooserDir.setCurrentDirectory(new File(userDir + "/Desktop")); // Set to User's Desktop
		chooserDir.setDialogTitle("Select Folder"); // GUI Title
		chooserDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		dirDragDrop(mainPanel);
		
		JLabel lblErrorEachValue = new JLabel("Error: Each value must be different");
		lblErrorEachValue.setVisible(false);
		lblErrorEachValue.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblErrorEachValue.setBounds(59, 284, 424, 43);
		this.add(lblErrorEachValue);
		
		JLabel lblFolder = new JLabel("Folder:");
		lblFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFolder.setBounds(10, 3, 65, 30);
		this.add(lblFolder);
		lblFolder.setForeground(Color.BLACK);
		lblFolder.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		btnSelectFolder = new JButton("Select a Folder");
		btnSelectFolder.setBounds(80, 2, 452, 30);
		this.add(btnSelectFolder);
		btnSelectFolder.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int result = chooserDir.showSaveDialog(btnSelectFolder);

				if(result == JFileChooser.APPROVE_OPTION && chooserDir.getSelectedFile() != null)
				{
					mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					String dirSavePath = chooserDir.getSelectedFile().getPath();
					
					if(dirSavePath.compareTo("") != 0)
						btnSelectFolder.setText(dirSavePath);
						
					playlist = new ArrayList<MP3FileSet>();
					
					File folderOfSongs = new File(dirSavePath);
					File[] songsArray = folderOfSongs.listFiles();
					songsAra = new ArrayList<File>();
					
					for(int i = 0; i < songsArray.length; i++)
					{
						if(songsArray[i].getName().contains(".mp3"))
						{
							playlist.add(new MP3FileSet(songsArray[i]));
							songsAra.add(songsArray[i]);
						}
					}
					
					mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
		);
		btnSelectFolder.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFolder.setFocusPainted(false);
		btnSelectFolder.setDropTarget(new DropTarget(btnSelectFolder, dirDropListener));
		
		JLabel lblChooseTheOrder = new JLabel("Choose the Order of the new File Names");
		lblChooseTheOrder.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		lblChooseTheOrder.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseTheOrder.setBounds(62, 95, 418, 52);
		this.add(lblChooseTheOrder);
		
		JLabel lblNamingTitle = new JLabel("Title:");
		lblNamingTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNamingTitle.setBounds(195, 183, 78, 52);
		this.add(lblNamingTitle);
		lblNamingTitle.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		JLabel lblForIgnore = new JLabel("0 for ignore (Title is default)");
		lblForIgnore.setHorizontalAlignment(SwingConstants.CENTER);
		lblForIgnore.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblForIgnore.setBounds(62, 120, 418, 52);
		this.add(lblForIgnore);
		
		spinnerTitle = new JSpinner();
		spinnerTitle.setModel(new SpinnerNumberModel(1, 0, 3, 1));
		spinnerTitle.setValue(1);
		spinnerTitle.setBounds(276, 200, 47, 20);
		this.add(spinnerTitle);
		
		JLabel lblNamingArtisit = new JLabel("Artisit:");
		lblNamingArtisit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNamingArtisit.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNamingArtisit.setBounds(195, 212, 78, 52);
		this.add(lblNamingArtisit);
		
		spinnerArtist = new JSpinner();
		spinnerArtist.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		spinnerArtist.setBounds(276, 229, 47, 20);
		this.add(spinnerArtist);
		
		JLabel lblNamingAlbum = new JLabel("Album:");
		lblNamingAlbum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNamingAlbum.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNamingAlbum.setBounds(195, 242, 78, 52);
		this.add(lblNamingAlbum);
		
		spinnerAlbum = new JSpinner();
		spinnerAlbum.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		spinnerAlbum.setBounds(276, 259, 47, 20);
		this.add(spinnerAlbum);
		
		JButton btnChangeNames = new JButton("Change Names");
		btnChangeNames.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				SaveFiles saving = new SaveFiles();
				
				boolean check = saving.checkOrders((int) spinnerTitle.getValue(), (int) spinnerArtist.getValue(), (int) spinnerAlbum.getValue());
				
				if(check == false && (int) spinnerTitle.getValue() == 0 && (int) spinnerArtist.getValue() == 0 && (int) spinnerAlbum.getValue() == 0)
				{
					check = true;
					spinnerTitle.setValue(1);
				}
				
				if(check == true && songsAra != null)
				{
					for(int i = 0; i < songsAra.size(); i++)
					{
						File newFile = saving.nameChange(songsAra.get(i), playlist.get(i), (int) spinnerTitle.getValue(), (int) spinnerArtist.getValue(), (int) spinnerAlbum.getValue());
						songsAra.set(i, newFile);
					}
					
					lblErrorEachValue.setVisible(false);
				}
				
				else
					lblErrorEachValue.setVisible(true);
				
				mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
		);
		btnChangeNames.setFocusPainted(false);
		btnChangeNames.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnChangeNames.setBounds(201, 330, 140, 52);
		this.add(btnChangeNames);
		
		this.setVisible(true);
	}
	
	public void resetInfo()
	{
		spinnerTitle.setValue(1);
		spinnerArtist.setValue(0);
		spinnerAlbum.setValue(0);
		
		btnSelectFolder.setText("Select a Folder");
	}
	
	private void dirDragDrop(JPanel mainPanel)
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
							
							if(files.get(0).isDirectory())
							{
								mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								
								String dirSavePath = files.get(0).getPath();
								
								if(dirSavePath.compareTo("") != 0)
									btnSelectFolder.setText(dirSavePath);
									
								playlist = new ArrayList<MP3FileSet>();
								
								File folderOfSongs = new File(dirSavePath);
								File[] songsArray = folderOfSongs.listFiles();
								songsAra = new ArrayList<File>();
								
								for(int i = 0; i < songsArray.length; i++)
								{
									if(songsArray[i].getName().contains(".mp3"))
									{
										playlist.add(new MP3FileSet(songsArray[i]));
										songsAra.add(songsArray[i]);
									}
								}
								
								mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
}
