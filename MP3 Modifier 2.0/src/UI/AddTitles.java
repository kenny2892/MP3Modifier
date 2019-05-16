package UI;

import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Main.MP3FileSet;
import Main.SaveFiles;

public class AddTitles extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static JButton btnSelectFolder;
	private static JTextArea txtrFilesToUpdate;
	
	private static String dirPath;
	private static ArrayList<MP3FileSet> playlist = new ArrayList<MP3FileSet>();

	private DropTargetListener dirDropListener;
	
	public AddTitles(JPanel mainPanel)
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
		
		dirDragDrop();
		
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
				int result = chooserDir.showOpenDialog(btnSelectFolder);
				
				if(result == JFileChooser.APPROVE_OPTION)
				{
					txtrFilesToUpdate.setText("Files to Update:");
					playlist = new ArrayList<MP3FileSet>();
					
					dirPath = chooserDir.getSelectedFile().getPath();
					
					if(dirPath.compareTo("") != 0)
						btnSelectFolder.setText(dirPath);
					
					File folderOfSongs = new File(dirPath);
					File[] songsArray = folderOfSongs.listFiles();
					
					for(int i = 0; i < songsArray.length; i++)
					{
						if(songsArray[i].getName().contains(".mp3"))
						{
							MP3FileSet temp = new MP3FileSet(songsArray[i]);
							
							if(temp.getTitle() == "" || temp.getTitle() == null)
							{
								playlist.add(temp);
								txtrFilesToUpdate.setText(txtrFilesToUpdate.getText() + "\n" + temp.getFile().getName());
							}
						}
					}
				}
			}
		}
		);
		btnSelectFolder.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFolder.setFocusPainted(false);
		btnSelectFolder.setDropTarget(new DropTarget(btnSelectFolder, dirDropListener));
		
		JTextArea txtrWillAddTitles = new JTextArea();
		txtrWillAddTitles.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		txtrWillAddTitles.setOpaque(false);
		txtrWillAddTitles.setEditable(false);
		txtrWillAddTitles.setText("Will add Titles to MP3 files lacking one\n          Based off of the File Name");
		txtrWillAddTitles.setBounds(66, 63, 410, 64);
		this.add(txtrWillAddTitles);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 158, 481, 232);
		this.add(scrollPane);
		
		txtrFilesToUpdate = new JTextArea();
		txtrFilesToUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		txtrFilesToUpdate.setText("Files to Update");
		scrollPane.setViewportView(txtrFilesToUpdate);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setFocusPainted(false);
		btnUpdate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				for(int i = 0; i < playlist.size(); i++)
				{
					SaveFiles saving = new SaveFiles();
					saving.addTitle(playlist.get(i));
				}
			}
		}
		);
		btnUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnUpdate.setBounds(209, 434, 124, 44);
		this.add(btnUpdate);
		
		this.setVisible(true);
	}
	
	public void resetInfo()
	{
		btnSelectFolder.setText("Files to Update");
		txtrFilesToUpdate.setText("Files to Update:");
	}
	
	private void dirDragDrop()
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
								txtrFilesToUpdate.setText("Files to Update:");
								playlist = new ArrayList<MP3FileSet>();
								
								dirPath = files.get(0).getPath();
								
								if(dirPath.compareTo("") != 0)
									btnSelectFolder.setText(dirPath);
								
								File folderOfSongs = new File(dirPath);
								File[] songsArray = folderOfSongs.listFiles();
								
								for(int i = 0; i < songsArray.length; i++)
								{
									if(songsArray[i].getName().contains(".mp3"))
									{
										MP3FileSet temp = new MP3FileSet(songsArray[i]);
										
										if(temp.getTitle() == "" || temp.getTitle() == null)
										{
											playlist.add(temp);
											txtrFilesToUpdate.setText(txtrFilesToUpdate.getText() + "\n" + temp.getFile().getName());
										}
									}
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
}
