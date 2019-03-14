package v3;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;

/**
* Just a small class for fetching the latest version if an update is needed.
* I should /probably/ rename it...
*/
class c implements ActionListener {
	static URI nextVersionUri;
	static JDialog dia;
	
	/**
	* @params t: the dialog that houses the update notice so that we can close this is the user
	* cancels or downloads the update.
	*/
	public c(JDialog t)
	{
		dia = t;
	}
	
	// Get's the OS as a string used in file naming
	static String GetOS()
	{
		if(Parser.OS.toLowerCase().contains("win"))
		{
			return "Windows";
		}else if(Parser.OS.toLowerCase().contains("nix")||Parser.OS.toLowerCase().contains("nux"))
		{
			return "Linux";
		}else if(Parser.OS.toLowerCase().contains("mac"))
		{
			return "Mac";
		}else
		{
			return "full";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getActionCommand())
		{
			case "Update!":
			// If we found an update, fetch the package as a zip stored on my package site.
			try {
				nextVersionUri =  new URI("http://ufvedp.000webhostapp.com/latest_"+GetOS()+".zip");
				open(nextVersionUri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			default:
				dia.dispose();
				
		}
	}
	private void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
}
