/**
 * 
 */


package v3;

import java.net.URL;

import javax.swing.ImageIcon;

public class Icons {
	private static ImageIcon i_exclamation = null;
	private static ImageIcon i_error = null;
	private static ImageIcon i_icon = null;
	private static ImageIcon i_save = null;
	private static ImageIcon i_load = null;
	private static ImageIcon i_close = null;
	private static ImageIcon i_exit = null;
	private static ImageIcon i_warning = null;
	private static ImageIcon i_question = null;
	
	ImageIcon[] i_others = new ImageIcon[30];

	private String[] paths = new String[0];
	

	public static final int[] OTHER = new int[] {
			0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29
	};
	public static final int EXCLAMATION = 30;
	public static final int WARNING = 31;
	public static final int ERROR = 32;
	public static final int SAVE = 33;
	public static final int LOAD = 34;
	public static final int CLOSE = 35;
	public static final int EXIT = 36;
	public static final int CANCEL = 37;
	public static final int ICON = 38;
	public static final int QUESTION = 39;
	
	public Icons()
	{
		buildIcons();
	}

	/**
	 * Icons
	 * @param s
	 * Takes in a string array for the files.  These paths should start with ".." + file.separator if the directory inside the jar is
	 * not housed in the package folder.  Note: order matters, and the value you wish to access from the custom arrays should match your input for the
	 * "Other" final array values.  You may include up to 30 custom icons to pick from.  Creating an instance will only vary the custom icons, not the predefined icons
	 * eg: i_exclamation
	 */
	public Icons(String[] paths)
	{
		this.paths = paths;
		buildIcons();
	}
	
	/**
	 * GetIcons
	 * @param i
	 * 
	 * To be used with the final integers in the Icons Class
	 * Retrieves from within the JAR file, in the Icons folder, the icon corresponding to the parameter.
	 */
	public ImageIcon GetIcons(int i)
	{
		switch(i)
		{
			case EXCLAMATION:
				return i_exclamation;
			case ICON:
				return i_icon;
			case WARNING:
				return i_warning;
			case ERROR:
				return i_error;
			case SAVE:
				return i_save;
			case LOAD:
				return i_load;
			case CLOSE:
				return i_close;
			case EXIT:
				return i_exit;
			case QUESTION:
				return i_question;
			default:
				return i_others[i];
		}
	}
	private void buildIcons() {
		String dir ="Icons/";
		String filePath = "";
		URL url;
	//case EXCLAMATION:
		filePath = dir+"Exclamation.png";
		url = Icons.class.getResource(filePath);
		
		if(url != null)
			i_exclamation = new ImageIcon(url);
		//i_exclamation = new javax.swing.ImageIcon(getClass().getResource(filePath));
		
	//case ICON:
		filePath = dir+"Icon.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_icon = new ImageIcon(url);
	
	//case WARNING:
		filePath = dir+"Warning.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_warning = new ImageIcon(url);
	
	//case ERROR:
		filePath = dir+"Error.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_error = new ImageIcon(url);
	
	//case SAVE:
		filePath = dir+"Save.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_save = new ImageIcon(url);
	
	//case LOAD:
		filePath = dir+"Load.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_load = new ImageIcon(url);
	
	//case CLOSE:
		filePath = dir+"Close.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_close = new ImageIcon(url);
	
	//case EXIT:
		filePath = dir+"Exit.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_exit = new ImageIcon(url);
	
	//case QUESTION:
		filePath = dir+"Question.png";
		url = Icons.class.getResource(filePath);
		if(url != null)
			i_question = new ImageIcon(url);
	
	//case OTHER:
		for(int i = 0; i < paths.length; i++)
		{
			if(paths[i] == null || paths[i].equals(""))
				continue;
			filePath = dir+paths[i];
			url = Icons.class.getResource(filePath);
			if(url != null)
				i_others[i] = new ImageIcon(url);
		}
		
	}
}
