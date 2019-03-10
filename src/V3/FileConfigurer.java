package v3;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class FileConfigurer
{

	public static void SetOwnerWritable(File f,boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OWNER_WRITE);
		else
			permissions.remove(PosixFilePermission.OWNER_WRITE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}
	
	public static void SetOwnerReadable(File f,boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OWNER_READ);
		else
			permissions.remove(PosixFilePermission.OWNER_READ);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}

	public static void SetOwnerExecutable(File f,boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OWNER_EXECUTE);
		else
			permissions.remove(PosixFilePermission.OWNER_EXECUTE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}
	
	public static void SetGroupReadable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.GROUP_READ);
		else
			permissions.remove(PosixFilePermission.GROUP_READ);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}

	public static void SetGroupWritable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.GROUP_WRITE);
		else
			permissions.remove(PosixFilePermission.GROUP_WRITE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
	}

	public static void SetGroupExecutable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.GROUP_EXECUTE);
		else
			permissions.remove(PosixFilePermission.GROUP_EXECUTE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}
	
	public static void SetOtherReadable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OTHERS_READ);
		else
			permissions.remove(PosixFilePermission.OTHERS_READ);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}

	public static void SetOtherWritable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OTHERS_WRITE);
		else
			permissions.remove(PosixFilePermission.OTHERS_WRITE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
	}

	public static void SetOtherExecutable(File f, boolean b) throws IOException
	{
		if(Parser.OS.contains("win")) {
			Runtime.getRuntime().exec("attrib -r " + f.getPath());
			return;
		}
		
		Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		
		if(b) 
			permissions.add(PosixFilePermission.OTHERS_EXECUTE);
		else
			permissions.remove(PosixFilePermission.OTHERS_EXECUTE);
		
		Files.setPosixFilePermissions(Paths.get(f.getAbsolutePath()), permissions);
		
	}
	
	public static void setCHMOD(File f, String s) throws IOException
	{
		setAll(f,false);
		
		for(int x = 0; x < 3; x++)
			{
				switch(x)
				{
					case 0:
						switch(s.charAt(x))
						{
							case 1:
								SetOwnerExecutable(f,true);
								SetOwnerWritable(f,false);
								SetOwnerReadable(f,false);
								break;
							case 2:
								SetOwnerExecutable(f,false);
								SetOwnerWritable(f,true);
								SetOwnerReadable(f,false);
								break;
							case 3:
								SetOwnerExecutable(f,true);
								SetOwnerWritable(f,true);
								SetOwnerReadable(f,false);
								break;
							case 4:
								SetOwnerExecutable(f,false);
								SetOwnerWritable(f,false);
								SetOwnerReadable(f,true);
								break;
							case 5:
								SetOwnerExecutable(f,true);
								SetOwnerWritable(f,false);
								SetOwnerReadable(f,true);
								break;
							case 6:
								SetOwnerExecutable(f,true);
								SetOwnerWritable(f,true);
								SetOwnerReadable(f,false);
								break;
							case 7:
								SetOwnerExecutable(f,true);
								SetOwnerWritable(f,true);
								SetOwnerReadable(f,true);
								break;
						}
						break;
					case 1:
						switch(s.charAt(x))
						{
							case 1:
								SetGroupExecutable(f,true);
								SetGroupWritable(f,false);
								SetGroupReadable(f,false);
								break;
							case 2:
								SetGroupExecutable(f,false);
								SetGroupWritable(f,true);
								SetGroupReadable(f,false);
								break;
							case 3:
								SetGroupExecutable(f,true);
								SetGroupWritable(f,true);
								SetGroupReadable(f,false);
								break;
							case 4:
								SetGroupExecutable(f,false);
								SetGroupWritable(f,false);
								SetGroupReadable(f,true);
								break;
							case 5:
								SetGroupExecutable(f,true);
								SetGroupWritable(f,false);
								SetGroupReadable(f,true);
								break;
							case 6:
								SetGroupExecutable(f,true);
								SetGroupWritable(f,true);
								SetGroupReadable(f,false);
								break;
							case 7:
								SetGroupExecutable(f,true);
								SetGroupWritable(f,true);
								SetGroupReadable(f,true);
								break;
						}
						break;
					case 2:
						switch(s.charAt(x))
						{
							case 1:
								SetOtherExecutable(f,true);
								SetOtherWritable(f,false);
								SetOtherReadable(f,false);
								break;
							case 2:
								SetOtherExecutable(f,false);
								SetOtherWritable(f,true);
								SetOtherReadable(f,false);
								break;
							case 3:
								SetOtherExecutable(f,true);
								SetOtherWritable(f,true);
								SetOtherReadable(f,false);
								break;
							case 4:
								SetOtherExecutable(f,false);
								SetOtherWritable(f,false);
								SetOtherReadable(f,true);
								break;
							case 5:
								SetOtherExecutable(f,true);
								SetOtherWritable(f,false);
								SetOtherReadable(f,true);
								break;
							case 6:
								SetOtherExecutable(f,true);
								SetOtherWritable(f,true);
								SetOtherReadable(f,false);
								break;
							case 7:
								SetOtherExecutable(f,true);
								SetOtherWritable(f,true);
								SetOtherReadable(f,true);
								break;
						}
						break;
				}
			}
		
	}

	public static void setAll(File f, boolean b) throws IOException
	{
		SetOwnerExecutable(f,b);
		SetOwnerReadable(f,b);
		SetOwnerWritable(f,b);

		SetGroupExecutable(f,b);
		SetGroupReadable(f,b);
		SetGroupWritable(f,b);

		SetOtherExecutable(f,b);
		SetOtherReadable(f,b);
		SetOtherWritable(f,b);
	}
}
