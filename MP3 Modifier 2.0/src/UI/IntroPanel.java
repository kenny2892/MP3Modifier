package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class IntroPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public IntroPanel(JPanel mainPanel)
	{
		this.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		this.setBounds(347, 234, 324, 93);
		mainPanel.add(this);
		this.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblSelectAnAction = new JLabel("Select an Action!");
		lblSelectAnAction.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		lblSelectAnAction.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblSelectAnAction);
		
		this.setVisible(false);
	}
}
