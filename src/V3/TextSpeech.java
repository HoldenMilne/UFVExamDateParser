package v3;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextSpeech 
{
	     VoiceManager freettsVM;
	     Voice freettsVoice;
	     private Voice voice;
	     
	     public TextSpeech(String voiceName) 
	     {
	          VoiceManager voiceManager = VoiceManager.getInstance();
	          freettsVoice = voiceManager.getVoice(voiceName);
	     }
	     
	     public void speak(String msg) {
	    	 freettsVoice.allocate();
	         freettsVoice.speak(msg);
	         //freettsVoice.deallocate();
	     }
	 
    
 
}
