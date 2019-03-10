package v3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HOptionPane extends JOptionPane implements KeyListener {
	
	Object response;
	public HOptionPane(JPanel panelout)
	{
		response = HOptionPane.showInputDialog(null, panelout, "", JOptionPane.PLAIN_MESSAGE,null,/*JOptionPane.QUESTION_MESSAGE, icons.GetIcons(Icons.QUESTION),*/ new String[] {"Default","Geometrix","Space","Night","Forest","Simple","Contrast"}, 0);
		
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		char c = arg0.getKeyChar();
		System.out.println(c);
		if(arg0.isControlDown())
		if(c == 'a')
		{
			Parser.voice.speak(Build.getAboutText());
		}else if(c == 'h')
		{
			Parser.voice.speak(Build.getHelpText());
		}else if(c == 't')
		{
			Parser.voice.speak(Build.getTosText());
		}else if(c == 's')
			Parser.soundOn = !Parser.soundOn;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
