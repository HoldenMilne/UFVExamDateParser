package v3;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
* The original not-very-portable colorscheme class.  It's not as useful as my newer one
* since I can only hold the 6 colors in the class for any scheme, as opposed to allowing for 
* n colors, depending on the scheme.  The new version will allow for more dynamic schemes.
*/
public class ColorScheme {
	
	public static ArrayList<ColorScheme> schemes = new ArrayList<ColorScheme>();
	public static ArrayList<ColorScheme> builtInSchemes = new ArrayList<ColorScheme>();
	
	
	final static String RIGHT = "right";
	final static String LEFT = "left";
	final static String ACCENT1 = "accent1";
	final static String BAR = "bar";
	final static String ACCENT2 = "accent2";
	final static String FONT = "font";
	
	public MyColor c_right = new MyColor(0xD2DD7E,RIGHT); // Green
	public MyColor c_left = new MyColor(0x75C7B2,LEFT); // Teal
	public MyColor c_accent1 = new MyColor(0x304463,ACCENT1); // Blue
	public MyColor c_bar = new MyColor(0xFAE9C7,BAR); // Soft Pink
	public MyColor c_accent2 = new MyColor(0xE0647B,ACCENT2); // Hard Pink
	public MyColor c_font = new MyColor(0xE0647B,FONT); // Hard Pink
	public Color c_clear = new Color(255,255,255,0); // Hard Pink
	
	public static int colorCount = 6;
	
	public Font f1,f2,f3;
	
	public String name;
	
	// Class takes in a name and 6 colors.  cn naming is ambiguous, should fix that if I'm going to
	// hard set them to specific color types.
	public ColorScheme(String s, MyColor c1,MyColor c2,MyColor c3,MyColor c4, MyColor c5, MyColor c6)
	{
		name = s;
		c_right = c1;
		c_left = c2;
		c_accent1 = c3;
		c_bar = c4;
		c_accent2 = c5;
		c_font = c6;
	}
	
	// If necessary loads some schemes and then fetches a specific scheme by name
	public static ColorScheme SchemeFactory(String s)
	{

		if(builtInSchemes.size()==0)
			LoadDefaultSchemes();
		
		for(ColorScheme c : builtInSchemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		for(ColorScheme c : schemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		return builtInSchemes.get(0);
	}
	
	// Loads the my premade schemes as default.
	public static void LoadDefaultSchemes()
	{
		//Right Panel, Left Panel, Accent1, Bar, Accent2,Font e2b03d 71acc1
		//db - 2c3f5a tc -AF7o5c g - 1e6347 y - f7d755 b -569588 AF705c

		builtInSchemes.add(new ColorScheme("Default",new MyColor(0x26a033,RIGHT),new MyColor(0x066922,LEFT),new MyColor(0xffffff,ACCENT1),new MyColor(0x066922,BAR),new MyColor(0xffd455,ACCENT2),new MyColor(0xf1f1f1,FONT)));
		builtInSchemes.add(new ColorScheme("Geometrix",new MyColor(0xEEF0F8,RIGHT),new MyColor(0x71acc1,LEFT),new MyColor(0xa0a2af,ACCENT1),new MyColor(0x71acc1,BAR),new MyColor(0x58857e,ACCENT2),new MyColor(0xFAFAFA,FONT)));
		builtInSchemes.add(new ColorScheme("Space",new MyColor(0xffd6fa,RIGHT),new MyColor(0xfafafa,LEFT),new MyColor(0x6dc8f2,ACCENT1),new MyColor(0xffd6fa,BAR),new MyColor(0xf27db2,ACCENT2),new MyColor(0x1,FONT)));
		builtInSchemes.add(new ColorScheme("Simple",new MyColor(0xFEFBFC,RIGHT),new MyColor(0xef9b34,LEFT),new MyColor(0xffd6fa,ACCENT1),new MyColor(0xef9b34,BAR),new MyColor(0xef9b34,ACCENT2),new MyColor(0xffffff,FONT)));
		builtInSchemes.add(new ColorScheme("BW",new MyColor(0xFFFFFF,RIGHT),new MyColor(0x000000,LEFT),new MyColor(0xFFFFFF,ACCENT1),new MyColor(0x000000,BAR),new MyColor(0x000000,ACCENT2),new MyColor(0xFFFFFF,FONT)));
		builtInSchemes.add(new ColorScheme("Night",new MyColor(0x5f6c77,RIGHT),new MyColor(0xeeeeee,LEFT),new MyColor(0x6666b0,ACCENT1),new MyColor(0xeeeeee,BAR),new MyColor(0xaba0FF,ACCENT2),new MyColor(0x555555,FONT)));
	}
	
	
	
}
