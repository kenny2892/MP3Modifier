// Jesse Ross
// April 23, 2019

package Main;

public class MP3Mod 
{
	public static void main(String[] args) 
	{
		UI ui = new UI();
		MenuManager menu = new MenuManager(ui);
		
		ui.addManager(menu);
		menu.showIntro();
	}

}
