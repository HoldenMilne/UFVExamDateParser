package v3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyThread extends Thread{

	public boolean running = true;
			
			
		public void run()
		{
			if(Parser.soundOn)
			{
				if(Parser.soundFull)
					Parser.voice.speak("Sound is on.  Press Control S to turn it off or on again.  Press Control H to hear the help section.  Press Control A. to hear the About section.  Press Control T to hear terms of service.");
				else
					Parser.voice.speak("Sound is on.  Keybindings Still Pending.");
				
			}
			while(running)
			{
				;
			}
		}
		
		
	}
	

