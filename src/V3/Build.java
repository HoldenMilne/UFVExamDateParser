package v3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Build
{
	static String pseudonym = "name";
	static String email = "email";
	
	public static void Setup(String e, String n)
	{
		pseudonym = n;
		email = e;
	}
	
	public static String getHelpText()
	{
		
		return "To use this program, select the semester your course is in, the "
				+ "Course type (AGRI - Agriculture, PSYC - Psychology, and so fourth) "
				+ "type in the course number and the section that you're in and press "
				+ "Go!  Your results will be displayed in the results box.  The program "
				+ "does not /need/ an internet connection to run as it stores your last "
				+ "viewed exam sheet.  That said, since the official sheets are often "
				+ "changed, and since it only saves the last used file sheet, it is best "
				+ "to use this with a network connection to ensure accurate results.  "
				+ "\n\nThere are several modes in which this program can be run.  These modes only affect the color scheme, and serve "
				+ "to aid in readability.  Simple mode uses a simple 3 color scheme with no background image "
				+ "Contrast is simply black and white, and Forest is a earth tone scheme with no background.  The others will use "
				+ "a background and if that background is removed, the program will default to the simple mode."
				+ "\n\nIf you find any errors, please email me at " + email;
		
		
	}

	public static String getAboutText()
	{
		return "This free program is made by " + pseudonym + " in 2018. Copyright "
				+ pseudonym + ". The program looks up data on the official "
				+ "exam schedule for each semester by assuming that UFV's "
				+ "file naming policy will not change.  Logo was made using an image by Freepik.  Alert icon taken from icons8.com."
				+ "\n\nIf you find any errors, please email me at " + email;
	}

	public static void SetupDirectory()
	{
		String c = Parser.separator;
		Parser.Working_Directory = Paths.get(Parser.Home_Directory+c+Build.pseudonym+c+"Exam Date Lookup");
		//System.out.println(Parser.Working_Directory);
		if(!new File(Parser.Working_Directory.toString()).exists())
			new File(Parser.Working_Directory.toString()).mkdirs();
	}

	public static String getTosText() {
		return "Really, there's not much to it.  If you use this tool "
		+ "and miss your exam, you can't blame me, or anyone else "
		+ "for that.  That's a risk you take by using this program. "
		+ "\n\nBy clicking the \"OK\" button bellow, or by actively changing the TOS "
		+ "setting in the Settings file to \"YES\" you are agreeing that \n\n   A) "
		+ "You have read comepletely, and fully understand what this document states.\n\n   B) "
		+ "You will not hold anyone but yourself accountable for any issues "
		+ "that this program may cause.  \n\n   C) You can view this document from within the program by going file > TOS.\n\n"
		+ "Seriously.  Don't come whinning to me if you miss your exam.  Double check everything "
		+ "and make sure you know your exam date for SURE.  This is just "
		+ "a fun little program that I made.  So... Sorry?";
	}
}
