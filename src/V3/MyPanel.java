package v3;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyPanel extends JPanel
{
		private static final long serialVersionUID = 1L;
		public int dx;
		public static int windowWidth;
		public static int xr = 1;
		public static int yr = 1;
		BufferedImage im = null;
		
		public MyPanel(String imagePath)
		{
			dx = 0;
			try
			{
				//ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(filePath)));
				im = ImageIO.read(MyPanel.class.getResource(imagePath));
			} catch (IOException e)
			{
				
				e.printStackTrace();
			}
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			if(Parser.simpleMode>0 && Parser.simpleMode!=3 && Parser.simpleMode < 5) 
				this.setBackground(Parser.p.scheme.c_right);
			else {
				int divx;
				Image i;
				
				if(im.getHeight()<im.getWidth())
					{
						divx= im.getHeight()/xr;
						if(divx == 0)
							i = im.getScaledInstance(im.getWidth(),yr, BufferedImage.SCALE_DEFAULT);
						else
							i = im.getScaledInstance(im.getWidth()/divx,yr, BufferedImage.SCALE_DEFAULT);
					}
				else
					{
						divx= im.getWidth()/yr;
						System.out.println(im.getWidth()+" "+yr+" " +divx);
						i = im.getScaledInstance(xr,im.getHeight()/divx, BufferedImage.SCALE_DEFAULT);
					}
				
				int dy = i.getWidth(null)/2-windowWidth;
				
				if(im == null)
					this.setBackground(Parser.p.scheme.c_right);	
				else	
					g.drawImage(i, -dy, -dx, null);
			}
		}
} //draw image with this