package v3;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class NoticeCanvas extends JPanel {
	int x = 480,y=420;
    private Image myImage;
    public NoticeCanvas()
    {
    	try {
			myImage = ImageIO.read(NoticeCanvas.class.getResource("Backgrounds/notice.png")).getScaledInstance(x, y, BufferedImage.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    @Override
    protected void paintComponent(Graphics g){ 
        super.paintComponent(g);    
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(myImage, 0, 0, null);
    } 

    public void changeImage(Image img) {
        this.myImage = img;
        repaint();
    }
}
