package v3;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Loading extends Thread
{
	Parser p;
	public Loading(Parser parser)
	{
		p = parser;
	}
	
	class imagePanel extends JComponent{
		Image image;
		//ImageIcon image;
		//TODO: DO THIS WITH ALL THE DAMN BACKGROUNDS WOOOOOOOOO
		
		public imagePanel(Image i)
		{
			image = i;
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image, 0, 0,this);
		}
	}
	
	public void run()
	{
		try
			{
				Image i = null;
				String dir = "Backgrounds/";
				ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(dir+"loading.png")));

				i = i2.getImage();
				i = i.getScaledInstance(534, 364, Image.SCALE_SMOOTH);
				p.setUndecorated(true);
				p.setBackground(new Color(1f,1f,1f,0f));
				p.setPreferredSize(new Dimension(534,364));
				p.setContentPane(new imagePanel(i));
				
				p.pack();
				p.setLocationRelativeTo(null);
				p.setVisible(true);
				while(!Parser.isLoaded)
					{
						System.out.print(""); // I need this for some reason...
					}
				
				
			} catch (IllegalMonitorStateException e2)
			{
				e2.printStackTrace();
			}
		p.dispose();
	}
}
