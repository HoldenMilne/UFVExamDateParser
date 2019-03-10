package v3;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;

class c implements ActionListener {
	static URI nextVersionUri;
	static JDialog dia;
	public c(JDialog t)
	{
		dia = t;
	}
	
	static String GetOS()
	{
		if(Parser.OS.toLowerCase().contains("win"))
		{
			return "win";
		}else if(Parser.OS.toLowerCase().contains("nix")||Parser.OS.toLowerCase().contains("nux"))
		{
			return "uni";
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