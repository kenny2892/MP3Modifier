package Main;

import java.io.File;

public class SaveFiles 
{
	public File nameChange(File file, MP3FileSet mp3, int titleOrder, int artistOrder, int albumOrder)
	{
		boolean oneCheck = false, twoCheck = false;
		String newFilePath = file.getParent() + "/";
		
		// Getting tags
		String title = mp3.getTitle(), artist = mp3.getArtist(), album = mp3.getAlbum();
		
		if(title == "" || title == null)
			title = file.getName().replaceAll(".mp3", "");
		
		// First
		if(titleOrder == 1 || artistOrder == 1 || albumOrder == 1)
			oneCheck = true;
		
		if(titleOrder == 1)
			newFilePath += title;
		
		else if(artistOrder == 1)
			newFilePath += artist;
		
		else if(albumOrder == 1)
			newFilePath += album;
		
		// Second
		if((titleOrder == 2 || artistOrder == 2 || albumOrder == 2) && oneCheck == true)
		{
			newFilePath += " - ";
			twoCheck = true;
		}		

		if(titleOrder == 2)
			newFilePath += title;

		else if(artistOrder == 2)
			newFilePath += artist;

		else if(albumOrder == 2)
			newFilePath += album;
		
		// Third
		if((titleOrder == 3 || artistOrder == 3 || albumOrder == 3) && (twoCheck == true || oneCheck == true))
			newFilePath += " - ";	
		
		if(titleOrder == 3)
			newFilePath += title;

		else if(artistOrder == 3)
			newFilePath += artist;

		else if(albumOrder == 3)
			newFilePath += album;
		
		// Putting it all together		
		File newFile = new File(newFilePath + ".mp3");
		newFile = new File(newFile.getParent() + "/" + newFile.getName().replaceAll(":", "-"));
		file.renameTo(newFile);
		
		return newFile;
	}
	
	public boolean checkOrders(int num1, int num2, int num3)
	{
		if(num1 == 0 && num2 == 0 && num3 == 0)
			return false;
		
		else if((num1 == 0 && num2 == 0 && num3 != 0) || (num1 == 0 && num2 != 0 && num3 == 0) || (num1 != 0 && num2 == 0 && num3 == 0))
			return true;
		
		else if(num1 != num2 && num1 != num3 && num2 != num3)
			return true;
		
		return false;
	}
	
	public void addTitle(MP3FileSet mp3)
	{
		mp3.setTitle(mp3.getFile().getName().replaceAll(".mp3", ""));
		
		try 
		{
			mp3.getAudioFile().save();
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
