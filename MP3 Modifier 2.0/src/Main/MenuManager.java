package Main;

public class MenuManager 
{
	private UI ui;
	
	public MenuManager(UI ui)
	{
		this.ui = ui;
	}
	
	public void hideAll()
	{
		ui.getIntro().setVisible(false);
		
		ui.getPlay().setVisible(false);
		ui.getPlay().hideInfo();
		ui.getPlay().resetInfo();
		
		ui.getModify().setVisible(false);
		ui.getModify().hideModPanel();
		ui.getModify().resetInfo();
		
		ui.getNameChange().setVisible(false);
		ui.getNameChange().resetInfo();
		
		ui.getAddTitles().setVisible(false);
		ui.getAddTitles().resetInfo();
	}
	
	public void showIntro()
	{
		hideAll();
		ui.getIntro().setVisible(true);
	}
	
	public void showPlay()
	{
		hideAll();
		ui.getPlay().setVisible(true);
	}
	
	public void showModify()
	{
		hideAll();
		ui.getModify().setVisible(true);
		ui.getModify().showModPanel();
	}
	
	public void showNameChange()
	{
		hideAll();
		ui.getNameChange().setVisible(true);
	}
	
	public void showAddTitles()
	{
		hideAll();
		ui.getAddTitles().setVisible(true);
	}
}
