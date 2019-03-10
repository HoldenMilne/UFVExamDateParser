package v3;


import java.io.File;

public class FileFilter extends javax.swing.filechooser.FileFilter{
	
	private String description;

	public FileFilter()
	{
	}
	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		return f.isDirectory();
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Directories";
	}
	
}
