package Main;

import java.awt.Image;
import java.io.File;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import jaco.mp3.player.MP3Player;

public class MP3FileSet 
{
	private MP3File audioFile = null;
	private ID3v24Tag v24tag = null;
	
	private String title, artist, album, genre, lyrics, comment;
	private int length, currentTime;
	private Image cover = null;
	
	private MP3Player song;
	private File fileMP3;
	
	public MP3FileSet(File file)
	{
		try
		{
			audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader audioInfo = (MP3AudioHeader) audioFile.getAudioHeader();
			//System.out.println("Song Length: " + audioInfo.getTrackLength());
			
			v24tag = audioFile.getID3v2TagAsv24();
			artist = v24tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
			title = v24tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
			album = v24tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
			Artwork image = v24tag.getFirstArtwork();
			
			if(image != null)
				cover = (Image) image.getImage();
			
			lyrics = v24tag.getFirst(FieldKey.LYRICS);
			comment = v24tag.getFirst(ID3v24Frames.FRAME_ID_COMMENT);
			genre = v24tag.getFirst(FieldKey.GENRE);
			length = audioInfo.getTrackLength();
			
			song = new MP3Player(file);
			setVolume(50);
			
			fileMP3 = file;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		currentTime = 0;
	}
	
	public MP3Player getSong()
	{
		return song;
	}
	
	public void stop()
	{
		song.stop();
	}
	
	public void play()
	{
		song.play();
	}
	
	public void pause()
	{
		song.pause();
	}
	
	public int getCurrentTimeInt()
	{
		return currentTime;
	}

	public MP3File getAudioFile() 
	{
		return audioFile;
	}

	public void setAudioFile(MP3File audioFile) 
	{
		this.audioFile = audioFile;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
		try 
		{
			v24tag.setField(FieldKey.TITLE, title);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
//			System.out.println(e.getMessage());
		}
	}

	public String getArtist() 
	{
		return artist;
	}

	public void setArtist(String artist) 
	{
		this.artist = artist;
		try 
		{
			v24tag.setField(FieldKey.ARTIST, artist);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
	}

	public String getAlbum() 
	{
		return album;
	}

	public void setAlbum(String album) 
	{
		this.album = album;
		try 
		{
			v24tag.setField(FieldKey.ALBUM, album);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
	}

	public String getGenre() 
	{
		return genre;
	}

	public void setGenre(String genre) 
	{
		this.genre = genre;
		try 
		{
			v24tag.setField(FieldKey.GENRE, genre);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
	}

	public String getLyrics() 
	{
		String mod = lyrics.replaceAll("\r", "\n");
		return mod;
	}

	public void setLyrics(String lyrics2) 
	{
		this.lyrics = lyrics2.replaceAll("\n", "\r");
		try 
		{
			v24tag.deleteField(FieldKey.LYRICS);
			v24tag.setField(FieldKey.LYRICS, lyrics);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			//System.out.println("e");
		}
	}

	public String getComment() 
	{
		return comment;
	}

	public void setComment(String comment) 
	{
		this.comment = comment;
		try 
		{
			v24tag.setField(FieldKey.COMMENT, comment);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
		}
	}

	public Image getCover() 
	{
		return cover;
	}

	public void setCover(File cover) 
	{
		Artwork pic = v24tag.getFirstArtwork();
		try 
		{
			pic = ArtworkFactory.createArtworkFromFile(cover);
			v24tag.deleteArtworkField();
			v24tag.setField(pic);
			audioFile.setTag(v24tag);
			audioFile.commit();
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public String getCurrentTime()
	{
		return convertToTime(currentTime);
	}
	
	public void setCurrentTime(int num)
	{
		this.currentTime = num;
	}
	
	public String getTotalTime()
	{
		return convertToTime(length);
	}
	
	private String convertToTime(int time)
	{
		int length = time;
		length = length / 60;
		int remainder = time % 60;
		
		String remainderStr = remainder + "";
		
		if(remainderStr.length() != 2)
			remainderStr = 0 + remainderStr;
		
		return length + ":" + remainderStr;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public void setVolume(int volume)
	{
		if(volume >= 0 && volume <= 100)
			song.setVolume(volume);
	}
	
	public File getFile()
	{
		return fileMP3;
	}
	
	public void setFile(File file)
	{
		fileMP3 = file;
	}
}
