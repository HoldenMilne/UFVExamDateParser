package v3;

public class VocalThread extends Thread {

	String msg;
	@Override
	public void run()
	{
		if(msg != null)
			Parser.voice.speak(msg);
	}
	
	public VocalThread(String message)
	{
		msg = message;
	}
}
