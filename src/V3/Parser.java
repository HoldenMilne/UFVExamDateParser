package v3;
/**
 * @Author Lex Icon
 * If you're reading this, you probably shouldn't be.  I know my code looks like shit don't judge me.
 * 
 * This version attempts to implement audio assisted navigation.  Adds mouse listeners to all items.  Plays a
 * sound bit for those.  Also updates read access on new files (background.jpg)
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.PlainDocument;

public class Parser extends JFrame implements ActionListener, KeyListener, MouseListener, WindowListener, FocusListener
{
	private static HTMLSourceReader reader1 = new HTMLSourceReader(new String[] {"<td>","<td height=\"20\">"},
			new String[] {"<strong>"},"<tr height=\"20\" style=\"height:15.0pt\">",
			"</tr>",","
	);

	private static final long serialVersionUID = 1L;
	public static final boolean soundOnDebug = false;
	
	Worksheet worksheet;
	public ColorScheme scheme;
	private static boolean TOS = false;
	private static long lastModified;
	private static String lastURL;
	private JTextField number;
	private JTextField section;
	private static JTextArea output;
	private JComboBox<String> coursesBox;
	private JComboBox<String> semesterBox;
	private JScrollPane oScroll;
	public static Parser p;
	private static int windowsCount = 0;
	Image im = null;
	static String OS = System.getProperty("os.name").toLowerCase();
	public static boolean isLoaded = false;
	JMenuItem sound;
	static boolean soundOn = false;
	static boolean soundFull = false;
	private static boolean connected;
	int out_y = 16;
	Font f_main;
	Font f_head;
	static JScrollPane jsp;

	public static TextSpeech voice = new TextSpeech("kevin16");
	
	static Icons icons = new Icons();
	
	static int simpleMode;
	
	static final String version = "EDP_V1_0_0";
	
	//files
	static String Hidden_Dir = "";
	static Path Home_Directory = Paths.get(System.getProperty("user.home"));
	static Path Working_Directory = Paths.get("Lex Icon" + File.separator+ "Exam Date Lookup");
	static final Path Res_Directory = Paths.get("res");
	static final Path Settings_Path = Paths.get("settings.edp");
	static File Settings_File;
	
	public static String separator = "\\";
	public static int lastWorkingDirectoriesCount = 0;
	public static String[] lastDirectories = new String[5];
	static Stack<File> filesUsed = new Stack<File>();
	public static void main(String[] args)
	{

		try {
			init();
		} catch (IOException e1) {System.out.println("ERROR AT INIT"); e1.printStackTrace();}
		p = new Parser("Home");

	}
	private static void GetSoundState() throws IOException
	{
		BufferedReader br = null;
		if(!soundOnDebug)
		{
			soundOn = false;
			return;
		}
		try
			{

				br = new BufferedReader(new FileReader(Settings_File));
				while(br.ready())
				{
					String line = br.readLine();
					if(line.contains("Sound"))
					{
						if(line.substring(line.lastIndexOf(":")+1).equalsIgnoreCase("true"))
						{
							soundOn = true;
						}
						else
						{
							soundOn = false;
						}

						break;
					}
				}
				
			} catch (IOException e)
			{
				e.printStackTrace();
			} 
	}
	private static boolean GetTOS() throws IOException
	{
		//Read from settings, i NO, show dialog, else return true.
		BufferedReader br = null;

		try
			{

				br = new BufferedReader(new FileReader(Settings_File));
				while(br.ready())
				{
					String line = br.readLine();
					if(line.contains("TOS"))
					{
						if(line.substring(line.lastIndexOf(":")+1).equalsIgnoreCase("yes"))
						{
							TOS = true;
						}
						else
						{
							TOS = false;
						}

						break;
					}
				}
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		
		if(TOS == true)
			return true;
		
		JPanel jpa = new JPanel();
		Dimension D = new Dimension(300,200);
		JTextArea jta = new JTextArea();
			jta.setText("Really, there's not much to it.  If you use this tool "
					+ "and miss your exam, you can't blame me, or anyone else "
					+ "for that.  That's a risk you take by using this program. "
					+ "\n\nBy clicking the \"OK\" button bellow, or by actively changing the TOS "
					+ "setting in the Settings file to \"YES\" you are agreeing that \n\n   A) "
					+ "You have read completely, and fully understand what this document states.\n\n   B) "
					+ "You will not hold anyone but yourself accountable for any issues "
					+ "that this program may cause.  \n\n   C) You can view this document from within the program by going file > TOS.\n\n"
					+ "Seriously.  Don't come whining to me if you miss you exam.  Double check everything "
					+ "and make sure you know your exam date for SURE.  This is just "
					+ "a fun little program that I made.  So... Sorry?");
			
			jta.setWrapStyleWord(true);
			jta.setLineWrap(true);
			jta.setEditable(false);
			jta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),BorderFactory.createEmptyBorder(3,6,3,6)));
			
			
		JScrollPane jsp = new JScrollPane();
			jsp.setViewportView(jta);
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(D);	
			
		jpa.add(jsp);
		//jpa.setMaximumSize(D);
		jpa.setBorder(BorderFactory.createTitledBorder("Agree to this half-assed TOS"));
		
		int approve = -1;
		if(icons.GetIcons(Icons.EXCLAMATION) == null)
			approve = JOptionPane.showConfirmDialog(null, jpa, "Agree to Terms Of Service", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
		else
			approve = JOptionPane.showConfirmDialog(null, jpa, "Agree to Terms Of Service", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,icons.GetIcons(Icons.EXCLAMATION));
		
		
		if(approve == JOptionPane.OK_OPTION)
			{
				TOS = true;
				
				return true;
			}
		
		
		
		return false;
	}


	private static String getLastURL() throws IOException
	{
		//FileConfigurer.SetOwnerReadable(Settings_File, true);
		BufferedReader br = new BufferedReader(new FileReader(Settings_File));
		while(br.ready())
			{
				String text = br.readLine();
				if(text.startsWith("Last URL:"))
					{
						text = text.substring(text.indexOf(":")+1);
						br.close();
						return text;
					}
			}

		//FileConfigurer.SetOwnerReadable(Settings_File, false);
		br.close();
		return "";
	}

	private static long GetLastModified() throws IOException
	{
		File f = new File(Working_Directory + File.separator + Res_Directory + File.separator + lastURL.substring(lastURL.lastIndexOf('/')+1, lastURL.lastIndexOf('.'))+".edp");

		if(!f.exists())
		{
			f.createNewFile();
			SettingsWrite(f);
		}

		//FileConfigurer.SetOwnerReadable(f, true);
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		while(br.ready())
			{
				String text = br.readLine();
				if(text.startsWith("Last Modified:"))
					{
						text = text.substring(text.indexOf(":")+1);
						br.close();
						return Long.valueOf(text);
					}
			}
		br.close();

		//FileConfigurer.SetOwnerReadable(f, false);
		
		return 0;
	}

	private static void SettingsWrite(File f) throws IOException
	{
		// Overloads from Settings write, where the non-parameter version defaults to SETTINGS_FILE and this takes in a file
		
		if(f.equals(Settings_File)) // If for whatever reason, settings file is passed in by mistake
			SettingsWrite();
		else
			{
				//FileConfigurer.SetOwnerWritable(f, true);
				BufferedWriter bufWrite = new BufferedWriter(new FileWriter(f));
				
				bufWrite.write("Last Modified:"+(-1));
			
				bufWrite.close();
				
				//FileConfigurer.SetOwnerWritable(Settings_File, false);
			}
	
	}

	private static void SettingsWrite() throws IOException {
		//FileConfigurer.SetOwnerWritable(Settings_File, true);
		BufferedWriter bufWrite = new BufferedWriter(new FileWriter(Settings_File));

		bufWrite.write("Last URL:" + lastURL + "\n"+ "TOS:"+((TOS)?"YES":"NO") +"\n"+ "Sound:"+soundOn);
	
		bufWrite.close();
		
		//FileConfigurer.SetOwnerWritable(Settings_File, false);
	}
	
	public static void init() throws IOException {
		
		Build.Setup(
				"holden.milne.p@gmail.com",
				"Holden Milne Pimentel"
				);
		
		separator = File.separator;
		
		
		Color bl = new Color(0x2566FB);
		Color pi = new Color(0xB633EF);
		Color whi1 = new Color(0xC9B1FF);
		Color whi2 = new Color(0x78C3FF);
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		FileFilter filter = new v2.FileFilter();
		
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(filter);
		
		// Get the hidden files directory and then read from the file
		String fileName = "";

		Working_Directory = Paths.get(Build.pseudonym + File.separator+ "Exam Date Lookup");
		
		if(OS.startsWith("win"))
			Hidden_Dir =  System.getenv("APPDATA")+separator+"Library"+separator+"Application Support"+separator+"Exam Date Lookup";
		else if(OS.endsWith("nix")||OS.endsWith("nux"))
			Hidden_Dir = System.getProperty("user.home")+separator+"."+Build.pseudonym;
		else
			Hidden_Dir = System.getProperty("user.home")+separator+".config"+separator+Build.pseudonym;
		File fileStream = new File(Hidden_Dir+separator+"dirs.txt");
		System.out.println(Hidden_Dir+ " << DIR");
		if(!fileStream.exists())
		{
			new File(Hidden_Dir).mkdirs();
			fileStream.createNewFile();
			fileStream.setReadable(true);
		}
		
		if(fileStream!=null)
		{
			
			BufferedReader br = new BufferedReader(new FileReader(fileStream));
				// Read first line to get count
				ArrayList<String> list = new ArrayList<String>();
				while(br.ready())
				{
					String str = br.readLine();
					list.add(str);
					lastWorkingDirectoriesCount +=1;
				}
				
				lastDirectories = new String[lastWorkingDirectoriesCount<5?lastWorkingDirectoriesCount:5];
				
				for(int i = 0; i < lastWorkingDirectoriesCount; i++)
					{
					lastDirectories[i] = list.get(i);
					}
				
				if(lastWorkingDirectoriesCount == 0 || (lastWorkingDirectoriesCount == 1 && lastDirectories[0].equals("system.home")))
				{
					lastDirectories = new String[] {Home_Directory.toString()};
				}
				
				JPanel FileChooserComponent = new JPanel();
				JComboBox<String> combo = new JComboBox<String>(lastDirectories);
				combo.setPreferredSize(new Dimension(240,22));
				JButton select = new JButton("Choose");
				select.addActionListener(new ActionListener(){
				
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						int x = jfc.showOpenDialog(null);
						
						if(x == JFileChooser.CANCEL_OPTION)
							return;
						
						if(new File(jfc.getSelectedFile().getPath()).isDirectory()&&!list.contains(jfc.getSelectedFile().getPath().toString()))
						{
							list.add("");
							if(list.size()>1) 
							{
								for(int i = list.size()-2; i > 0 ; i--)
								{
									list.set(i+1, list.get(i));
								}
								list.set(1, list.get(0));
								if(list.size()>5)
									list.remove(list.size()-1);
							}
							list.set(0,jfc.getSelectedFile().getPath());
							lastDirectories = new String[list.size()];
							for(int i = 0; i < list.size(); i++)
								lastDirectories[i] = list.get(i);
							
							combo.removeAllItems();
							for(String s : list)
								combo.addItem(s);
							
						}
						
					}
					
				});

				FileChooserComponent.add(combo);
				FileChooserComponent.add(select);
				
				int i = JOptionPane.showConfirmDialog(null, FileChooserComponent, "Select Main Directory", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,icons.GetIcons(Icons.QUESTION));
				if(i == JOptionPane.OK_OPTION)
					{
						Home_Directory = Paths.get(combo.getSelectedItem().toString());
					}
				else System.exit(0);
		}
				
		Settings_File = new File(Home_Directory+separator+Working_Directory + separator + Res_Directory + separator + Settings_Path);
		
		
		Build.SetupDirectory();
		ColorScheme.LoadDefaultSchemes();
		try
			{
				// Will be used for a Loading/Splash screen
				//String filePath = Res_Directory+File.separator+"Icons"+File.separator+"Warning.png";
				
				if(!Settings_File.exists())
					{
						JOptionPane.showMessageDialog(null, "Resource folder cannot be located and must be Built.", "Missing Directories",JOptionPane.INFORMATION_MESSAGE,icons.GetIcons(Icons.EXCLAMATION));
	
						File path = new File(Working_Directory.toString()+File.separator+Res_Directory);
						path.mkdirs();
						System.out.println(path.getAbsolutePath() + Settings_File.getPath());
						Settings_File = new File(path.getAbsolutePath()+separator+"settings.edp");
						Settings_File.createNewFile();
					}
				
				if(!GetTOS())
					System.exit(0);

				JPanel panelout = new JPanel();
				Dimension d = new Dimension(500,420);
				NoticeCanvas panelin = new NoticeCanvas();
				panelin.setPreferredSize(d);
				//panelin.getGraphics().drawImage(ImageIO.read(Parser.class.getResource("Backgrounds/notice.png")).getScaledInstance(x, y, Image.SCALE_FAST), x, y, Color.black, null);
				JScrollPane scrollPane = new JScrollPane(panelin);
				scrollPane.setPreferredSize(new Dimension(500,300));
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				JLabel label = new JLabel("Choose a Theme");
				
				panelout.add(scrollPane,BorderLayout.NORTH);
				panelout.add(label, BorderLayout.SOUTH);
				panelout.setPreferredSize(new Dimension(500,330));
				//panelout.setSize(300,300);

				lastURL = getLastURL();
				
				if(lastURL.equals("")||lastURL.equalsIgnoreCase("null")) {
					int month = (Calendar.getInstance().get(Calendar.MONTH));
					if(month <5)
						month = 1;
					else if(month<9)
						month = 5;
					else
						month = 9;
					lastURL="https://www.ufv.ca/arfiles/includes/"+(Calendar.getInstance().get(Calendar.YEAR))+"0"+month+"-exam-schedule.htm";
				}
				lastModified = GetLastModified();
				
				GetModified(lastURL);
				GetSoundState();
				SettingsWrite();
				MyThread t = new MyThread();
				t.start();
				// TODO: USE KEYBINDINGS NOT KEYLISTENER
				Object response = JOptionPane.showInputDialog(null, panelout, "", JOptionPane.PLAIN_MESSAGE,null,/*JOptionPane.QUESTION_MESSAGE, icons.GetIcons(Icons.QUESTION),*/ new String[] {"Default","Geometrix","Space","Night","Simple","Contrast"}, 0);
				//JOptionPane.
				t.running = false;
				//panelout.addKeyListener(
				if(response == null)
					System.exit(0);
				
				if(response.equals("Default"))
					simpleMode = 5;
				else if(response.equals("Geometrix"))
					simpleMode = 6;
				else if(response.equals("Forest"))
					simpleMode = 4;
				else if(response.equals("Night"))
					simpleMode = 3;
				else if(response.equals("Contrast"))
					simpleMode = 2;
				else if(response.equals("Simple"))
					simpleMode = 1;
				else simpleMode = 0;
				
				new Parser("Loading");
				//TODO: Set it so that Settings are stored in the first two lines of the 20xxyy-exam-schedule file and
				//		reading the file skips over it, unless looking for the settings.

			} catch (IOException e)
			{
				e.printStackTrace();
			}
				
	}

	public Parser(String string)
	{
		// Creates the Window to draw on
		super(string+((connected || !string.equals("Home"))?"":" - Not Connected"));
		this.setName(string+((connected || !string.equals("Home"))?"":" - Not Connected"));
		scheme = ColorScheme.SchemeFactory("Default");
		
		worksheet = new Worksheet();
		Loading t = null;
		System.out.println(OS);
		
		String filePath = "";
		try
			{
				String dir ="Backgrounds/";
				if(simpleMode == 6)
					{
						filePath = dir+"geometrix.jpg";
						ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(filePath)));

						im = i2.getImage();
					}
				else if(simpleMode == 3) 
					{
						filePath = dir+"night.jpg";
						ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(filePath)));

						im = i2.getImage();
						
						//im = ImageIO.read(new File(filePath));			
					}
				else if(simpleMode == 0)
					{
						filePath = dir+"space.jpg";
						ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(filePath)));

						im = i2.getImage();
					}
				else 
					{
						filePath = dir+"default.jpg";
						ImageIcon i2 = (new javax.swing.ImageIcon(getClass().getResource(filePath)));

						im = i2.getImage();
					}
			}finally {
				if(im == null)
					{
						if(simpleMode == 0 || simpleMode == 3)
							{
								simpleMode = 1;
								if(icons.GetIcons(Icons.ERROR) == null)
									JOptionPane.showMessageDialog(null, "The background for this scheme does not exist.  Please reinstall to use this scheme.", "Background missing", JOptionPane.ERROR_MESSAGE);
								else
									JOptionPane.showMessageDialog(null, "The background for this scheme does not exist.  Please reinstall to use this scheme.", "Background missing", JOptionPane.ERROR_MESSAGE, icons.GetIcons(Icons.ERROR));
							}
					}
			}
		InputStream is = Parser.class.getResourceAsStream("fonts/radiospace.ttf");
		Font font = null;
		Font defaultHeadFont = new Font("Cambria",1,14);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.BOLD, 16);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Font Load Failed");
			e.printStackTrace();
		} 
		System.out.println(simpleMode);
		if(simpleMode > 0) {
			f_head = defaultHeadFont;
			f_main = new Font("Cambria",Font.BOLD,12);
		}
		else if(OS.contains("win"))
		{
			if(font !=null)
				f_head = font;
			else f_head = defaultHeadFont;
			f_main = new Font("Bell MT",1,14);
			
		}else if(OS.contains("unix")||OS.contains("nux"))
		{
			if(font !=null)
				f_head = font;
			else font = defaultHeadFont;
			f_main = new Font("Cambria",Font.BOLD,12);
		}else
		{
			if(font !=null)
				f_head = font;
			else f_head = defaultHeadFont;
			f_main = new Font("Cambria",Font.BOLD,12);
		}
		
		if(simpleMode == 0)
			scheme = ColorScheme.SchemeFactory("Space");
		else if(simpleMode == 1)
			scheme = ColorScheme.SchemeFactory("Simple");
		else if(simpleMode == 2)
			scheme = ColorScheme.SchemeFactory("BW");
		else if(simpleMode == 3)
			scheme = ColorScheme.SchemeFactory("Night");
		else if(simpleMode == 4)
			scheme = ColorScheme.SchemeFactory("Forest");
		else if(simpleMode == 6)
			scheme = ColorScheme.SchemeFactory("Geometrix");
		else
			scheme = ColorScheme.SchemeFactory("Default");
		// Loading Screen Pop Up
		if(string.equals("Loading"))
			t = new Loading(this);
		
		Container contentPane = this.getContentPane();
		
		this.setResizable(false);
		if(icons.GetIcons(Icons.ICON)!=null)
			this.setIconImage(icons.GetIcons(Icons.ICON).getImage());
		switch(string)
		{
			case "Home":
				
				Dimension textDim = new Dimension(60,18);
				Dimension textDim2 = new Dimension(240,102);
				
				this.addKeyListener(this);
				
				String[] coursesArray = new String[] {
					"ADED","AGRI","AH","ANTH","ASTR","AV","BIO","BUS","CHEM","CIS","CMNS","COMP","CRIM","CSM","CYC",
					"ECE", "ECON", "ENGL","ENGR","ENPH","FILM","FNST","FREN","GD", "GDS","GEOG","GERM","HALQ","HIST","HSER","IPK","JAPN","JRNL",
					"KIN","KPE", "LAS","LIBT","LING","MACS","MAND","MATH","MENN","MUSC","PACS","PHIL","PHYS","POSC","PSYC","PUNJ",
					"RSS","RUSS","SOC","SOWK","SPAN","STAT","THEA","VA","WMST"
					};
				
				// JComboBoxes
				coursesBox = new JComboBox<String>(coursesArray);
					coursesBox.setPreferredSize(new Dimension(200,20));
					coursesBox.addMouseListener(this);
					coursesBox.addKeyListener(this);
					coursesBox.setName("Courses Box");
					coursesBox.setForeground(scheme.c_font);		
					coursesBox.setBackground(scheme.c_left);
					coursesBox.addFocusListener(this);
					
					
				semesterBox = new JComboBox<String>(GetSemestersArray());
					semesterBox.addFocusListener(this);
					semesterBox.setPreferredSize(new Dimension(200,20));
					semesterBox.addMouseListener(this);
					semesterBox.addKeyListener(this);
					semesterBox.setName("Semester Box");
					semesterBox.setForeground(scheme.c_font);		
					semesterBox.setBackground(scheme.c_left);
				// JTextAreas
				number = new JTextField();
					number.setBorder(BorderFactory.createLoweredSoftBevelBorder());
					number.setPreferredSize(textDim);
					number.addKeyListener(this);
					number.addMouseListener(this);
					number.setName("Course Number");
					number.addFocusListener(this);

					PlainDocument doc = (PlainDocument) number.getDocument();
					doc.setDocumentFilter(new CourseNumFilter());
					
				section = new JTextField();
					section.setBorder(BorderFactory.createLoweredSoftBevelBorder());
					section.setPreferredSize(textDim);
					section.addKeyListener(this);
					section.addMouseListener(this);
					section.setName("Section");
					section.addFocusListener(this);

					PlainDocument doc2 = (PlainDocument) section.getDocument();
					doc2.setDocumentFilter(new SectionFilter());
					
				output = new JTextArea();
					output.setBorder(BorderFactory.createLoweredSoftBevelBorder());
					output.setPreferredSize(new Dimension(210,82));
					output.setEditable(false);
					output.addFocusListener(this);
					output.setName("output");
					output.addMouseListener(this);
				oScroll = new JScrollPane(output);
				oScroll.setPreferredSize(textDim2);
				oScroll.getVerticalScrollBar().setOpaque(false);
				Color c = oScroll.getVerticalScrollBar().getForeground();
				c = new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(c.getAlpha()/2));
				
				oScroll.getVerticalScrollBar().setForeground(c);
				oScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				oScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				oScroll.getVerticalScrollBar().setUnitIncrement((int) ((out_y)));
				//oScroll.getVerticalScrollBar().setValue(0);
				
				// JButtons
				JButton get = new JButton("Go!");
					get.addActionListener(this);
					get.addMouseListener(this);
					get.addFocusListener(this);
					get.addKeyListener(this);
					get.setPreferredSize(new Dimension(80,26));
					get.setName("Go!");
					get.setFont(f_main);
					get.setForeground(scheme.c_font);
					get.setBackground(scheme.c_bar);
					BevelBorder be = new BevelBorder(BevelBorder.RAISED, scheme.c_bar, scheme.c_accent2);
					get.setBorder(be);

				// JPanels	
				JPanel semesterPan = new JPanel();
				JLabel labS1 = new JLabel("Semester");
					semesterPan.add(labS1);
					semesterPan.add(semesterBox);
					semesterPan.setBackground(scheme.c_right);
					semesterPan.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
					
					
				JPanel coursePan = new JPanel();
				JLabel labC1 = new JLabel("Course");
					labC1.setFont(f_main);
					coursePan.add(labC1);
					coursePan.add(coursesBox);
					coursePan.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
					coursePan.setBackground(scheme.c_right);
					
				JPanel numberPan = new JPanel();
				JLabel labC2 = new JLabel("Course Number");
					labC2.setFont(f_main);
					numberPan.add(labC2);
					numberPan.add(number);
					numberPan.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
					numberPan.setBackground(scheme.c_right);
					
				JPanel sectionPan = new JPanel();
					JLabel labS2 = new JLabel("Section");
							labS2.setFont(f_main);
					sectionPan.add(labS2);
					sectionPan.add(section);
					sectionPan.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
					sectionPan.setBackground(scheme.c_clear);
					
			
				
				MyPanel top = new MyPanel(filePath); 
				JPanel jp = new JPanel();
						
					jp.setLayout(new GridLayout(4,1));

					jp.add(semesterPan);
					jp.add(coursePan);
					jp.add(numberPan);
					jp.add(sectionPan);
					BevelBorder bev = new BevelBorder(BevelBorder.RAISED, scheme.c_accent2, scheme.c_accent2);
					TitledBorder tit = new TitledBorder(BorderFactory.createLineBorder(scheme.c_accent2), "Course Information", TitledBorder.LEFT, TitledBorder.TOP, f_head);
					jp.setBorder(BorderFactory.createCompoundBorder(bev,tit));
					jp.setBackground(scheme.c_right);
					top.add(jp);
				MyPanel center = new MyPanel(filePath);
				
					center.add(get);
					center.setBackground(scheme.c_accent2);
					
				MyPanel bottom = new MyPanel(filePath);
					
					JLabel jll = new JLabel("Result:");
					
					if(simpleMode == 0)
						jll.setForeground(scheme.c_font);
					else if(simpleMode == 5)
						jll.setForeground(scheme.c_accent2);
					else
						jll.setForeground(scheme.c_left);
					

					if(simpleMode == 5)
						{
						jp.setBackground(new Color(jp.getBackground().getRed(),jp.getBackground().getBlue(),
								jp.getBackground().getGreen(),jp.getBackground().getAlpha()/4));
						coursePan.setOpaque(false);
						numberPan.setOpaque(false);
						sectionPan.setOpaque(false);
						semesterPan.setOpaque(false);
						}
					bottom.add(jll);
					bottom.add(oScroll);
					bottom.setBackground(scheme.c_right);
					
				JMenuBar bar = new JMenuBar();;
					bar.setBackground(scheme.c_bar);
					bar.setForeground(scheme.c_font);
					
				JMenu file = new JMenu("File");
				file.setBackground(scheme.c_bar);
				file.setForeground(scheme.c_font);
					JMenuItem about = new JMenuItem("About");
						about.addActionListener(this);
						about.setBackground(scheme.c_bar);
						about.setForeground(scheme.c_font);
					JMenuItem help = new JMenuItem("Help");
						help.addActionListener(this);
						help.setBackground(scheme.c_bar);
						help.setForeground(scheme.c_font);
					JMenuItem TOS = new JMenuItem("TOS");
						TOS.addActionListener(this);
						TOS.setBackground(scheme.c_bar);
						TOS.setForeground(scheme.c_font);
					JMenuItem changes = new JMenuItem("Changes");
						changes.addActionListener(this);
						changes.setBackground(scheme.c_bar);
						changes.setForeground(scheme.c_font);
					JMenuItem exit = new JMenuItem("Exit");
						exit.addActionListener(this);
						exit.setBackground(scheme.c_bar);
						exit.setForeground(scheme.c_font);
					sound = new JMenuItem((soundOn)?"Sound - On":"Sound - Off");
						sound.addActionListener(this);
						sound.setBackground(scheme.c_bar);
						sound.setForeground(scheme.c_font);
						
				sound.setSelected(soundOnDebug);
				JMenu outputMenu = new JMenu("Output");
					outputMenu.setBackground(scheme.c_bar);
					outputMenu.setForeground(scheme.c_font);
					ExportActionListener eal = new ExportActionListener();
					JMenuItem addToList = new JMenuItem("Add");
						addToList.addActionListener(eal);
						addToList.setBackground(scheme.c_bar);
						addToList.setForeground(scheme.c_font);
					JMenuItem removeFromList = new JMenuItem("Remove");
						removeFromList.addActionListener(eal);
						removeFromList.setBackground(scheme.c_bar);
						removeFromList.setForeground(scheme.c_font);
					JMenuItem export = new JMenuItem("Export");
						export.addActionListener(eal);
						export.setBackground(scheme.c_bar);
						export.setForeground(scheme.c_font);
					JMenuItem view = new JMenuItem("View");
						view.addActionListener(eal);
						view.setBackground(scheme.c_bar);
						view.setForeground(scheme.c_font);
				outputMenu.add(addToList);
				outputMenu.add(removeFromList);
				outputMenu.add(export);
				outputMenu.add(view);
					
				bar.add(file);
				//bar.add(outputMenu);
				
				file.add(about);
				file.add(help);
				file.add(TOS);
				file.add(changes);
				if(soundOnDebug)
					file.add(sound);
				file.addSeparator();
				file.add(exit);
				
				this.setJMenuBar(bar);
				
				contentPane.add(top,BorderLayout.NORTH);
				contentPane.add(center,BorderLayout.CENTER);
				contentPane.add(bottom,BorderLayout.SOUTH);
				
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
				this.addWindowListener(this);
				BevelBorder bevBord = new BevelBorder(BevelBorder.RAISED, scheme.c_accent1, scheme.c_accent2);
				this.getRootPane().setBorder(bevBord);
				
				pack();
				
				setLocationRelativeTo(null);
				MyPanel.windowWidth = this.getWidth()/2;
				center.dx = top.getHeight();
				bottom.dx = (top.getHeight()+center.getHeight());
				MyPanel.xr = this.getWidth();
				MyPanel.yr = this.getHeight();
				
				try
				{
					Thread.sleep(2000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				file.requestFocus();
				
				isLoaded = true;
			
				this.setVisible(true);
				semesterBox.requestFocus();
			Thread updateThread = new Thread()
							{
								// TODO: Set up url for drive file with specific email
								private final String uri = "https://ufvedp.000webhostapp.com";
								// Set build version
								private final String VerClass = "win";
								private final Version ver = new Version();
								public JDialog dia;
								@Override
								public void run()
								{
									try {
										URL url = new URL(uri);
										
										// Check Connectivity
										URLConnection conn = url.openConnection();
										
								        String text = StreamToString(conn.getInputStream());

										if(text.equals("ERROR"))
										{
											System.out.println("Something went wrong . . .");
										}
										else if(text.equalsIgnoreCase(GetVersion()))
										{
											System.out.println("Up to date!  Fret not little one!");
										}else
										{
											System.out.println("Needs update!  Updating . . .");
											// Update
											JPanel panel = new JPanel();
											JLabel label = new JLabel("An update is available.  Click this button to download it.");
											panel.add(label,BorderLayout.NORTH);
											JButton button = new JButton("Update!");
											JButton button2 = new JButton("Don't Update :(");
											
											button.setPreferredSize(new Dimension(120,30));
											button2.setPreferredSize(new Dimension(120,30));
											
											//panel.setPreferredSize(new Dimension(360,60));
											JOptionPane jop = new JOptionPane();
											jop.setMessageType(JOptionPane.PLAIN_MESSAGE);
											jop.setMessage(panel);
											dia = jop.createDialog(null, "Update Available");
											button.addActionListener(new c(dia));
											button2.addActionListener(new c(dia));
											JPanel buttons = new JPanel();
											buttons.add(button);
											buttons.add(button2);
											if(p.scheme!=null)
											{
												label.setForeground(p.scheme.c_font);
												if(scheme.name.equals("Space"))
													panel.setBackground(p.scheme.c_bar);
												else
													panel.setBackground(p.scheme.c_left);
												jop.setBackground(panel.getBackground());
												dia.setBackground(panel.getBackground());
												button.setBorder(BorderFactory.createEmptyBorder());

												if(scheme.name.equals("Space")) {
													button.setBackground(p.scheme.c_accent2);
													button.setForeground(p.scheme.c_left);
												}else
												{
													button.setForeground(p.scheme.c_accent2);
													button.setBackground(p.scheme.c_right);
													
												}
												button2.setBorder(BorderFactory.createEmptyBorder());
												button2.setBackground(button.getBackground());
												button2.setForeground(button.getForeground());
												buttons.setBackground(panel.getBackground());
											}

											
											panel.add(buttons, BorderLayout.SOUTH);
											dia.setContentPane(panel);
											//dia.pack();
											dia.setVisible(true);
											
											}
										
										
									} catch (MalformedURLException e) {
										System.out.println("Error due to bad URL link: " +uri+".");
										e.printStackTrace();
									} catch (IOException e) {
										System.out.println("Error due to failed connection to: " +uri+". Check network.");
										e.printStackTrace();
									}
								}

								private String GetVersion() {
									// TODO Auto-generated method stub
									if(VerClass.equalsIgnoreCase("win"))
										return ver.win;
									else if(VerClass.equalsIgnoreCase("lin"))
										return ver.lin;
									else if (VerClass.equalsIgnoreCase("mac"))
										return ver.mac;
									return "";
								}

								public void closeDialog()
								{
									dia.dispose();
								}
								
								public String StreamToString(InputStream inputStream) throws IOException
								{
									BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
									String text = "";
									String OSFlag =  "";
									if(OS.toLowerCase().contains("win"))
										OSFlag = "win";
									else if(OS.toLowerCase().contains("unix")||OS.contains("nux"))
										OSFlag = "lin";
									else if(OS.toLowerCase().contains("mac"))
										OSFlag = "mac";
									
									while(br.ready())
									{
										text = br.readLine();
										if(text.startsWith("<ver_" + OSFlag+ ">"))
										{
											text = text.substring(text.indexOf("<ver_" + OSFlag + ">")+("<ver_"+OSFlag+">").length(), text.indexOf("</ver_"+OSFlag+">"));
											return text;
										}
											
									}
									
									return "ERROR";
									
								}
							};
				
						System.out.println("Update Start");
						updateThread.start();
				break;
				
			case "Help":
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
				JFormattedTextField helpHeader = new JFormattedTextField("HELP"); 
					helpHeader.setPreferredSize(new Dimension(260,70));
					helpHeader.setBorder(BorderFactory.createEmptyBorder(10,60,10,60));
					helpHeader.setFont(new Font("Arial Black",Font.BOLD,42));
					helpHeader.setDisabledTextColor(scheme.c_font);
					helpHeader.setEnabled(false);
					helpHeader.setBackground(scheme.c_bar);
					helpHeader.setEditable(false);
			
				JTextArea helpText = new JTextArea(); 
					helpText.setText(Build.getHelpText());
					helpText.setEditable(false);
					helpText.setLineWrap(true);
					helpText.setWrapStyleWord(true);
					helpText.setAlignmentX(CENTER_ALIGNMENT);
					helpText.setBounds(10, 10, 10, 10);
					helpText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				
				JScrollPane helpSP = new JScrollPane();
					helpSP.setViewportView(helpText);
					helpSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					helpSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					helpSP.setPreferredSize(new Dimension(240,160));
					helpSP.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
								JPanel helpTextPanel = new JPanel();
					helpTextPanel.add(helpSP);
					helpTextPanel.setBackground(scheme.c_bar);
					
				contentPane.add(helpHeader,BorderLayout.NORTH);
				contentPane.add(helpTextPanel,BorderLayout.SOUTH);
				
				this.addWindowListener(this);
				pack();
				helpSP.getVerticalScrollBar().setValue(helpSP.getVerticalScrollBar().getValue()-200);
				
				this.setLocationRelativeTo(p);
				this.setVisible(true);
				
				break;
			case "About":
				JFormattedTextField header = new JFormattedTextField("ABOUT"); 
					header.setPreferredSize(new Dimension(260,70));
					header.setBorder(BorderFactory.createEmptyBorder(10,44,10,44));
					header.setFont(new Font("Arial Black",Font.BOLD,42));
					header.setDisabledTextColor(scheme.c_font);
					header.setEnabled(false);
					header.setBackground(scheme.c_bar);
					header.setEditable(false);
				
				JTextArea aboutText = new JTextArea(); // use formatted Text instead TODO:
					aboutText.setText(Build.getAboutText());
					aboutText.setEditable(false);
					aboutText.setLineWrap(true);
					aboutText.setWrapStyleWord(true);
					aboutText.setAlignmentX(CENTER_ALIGNMENT);
					aboutText.setBounds(10, 10, 10, 10);
					aboutText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
					
				JScrollPane aboutSP = new JScrollPane();
					aboutSP.setViewportView(aboutText);
					aboutSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					aboutSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					aboutSP.setPreferredSize(new Dimension(240,160));
					aboutSP.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

					
				JPanel textPanel = new JPanel();
					textPanel.add(aboutSP);
					textPanel.setBackground(scheme.c_bar);
			
				contentPane.add(header,BorderLayout.NORTH);
				contentPane.add(textPanel,BorderLayout.SOUTH);

				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

				this.addWindowListener(this);
				pack();
				this.setLocationRelativeTo(p);
				this.setVisible(true);
				
				break;
			case "TOS":
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setTitle("Terms Of Service");
				JPanel jpa = new JPanel();
				jpa.setBackground(scheme.c_bar);
				Dimension D = new Dimension(300,200);
		
				JTextArea jta = new JTextArea();
					jta.setText(Build.getTosText());
					
					jta.setWrapStyleWord(true);
					jta.setLineWrap(true);
					jta.setEditable(false);
					jta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),BorderFactory.createEmptyBorder(3,6,3,6)));
					
				JScrollPane jsp = new JScrollPane();
					jsp.setViewportView(jta);
					jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					jsp.setPreferredSize(D);	
					
				jpa.add(jsp);
				
				contentPane.add(jpa);

				this.pack();
				this.setLocationRelativeTo(p);
				this.setVisible(true);
				break;
			case "Changes":
				try {
				    Desktop.getDesktop().browse(new URL("https://ufvedp.000webhostapp.com").toURI());
				} catch (Exception e) {e.printStackTrace();}
				break;
			case "Loading":
				t.start();
				break;
		}
		
	}
	
	private String[] GetSemestersArray()
	{
		int year = (Calendar.getInstance().get(Calendar.YEAR));
		int month = (Calendar.getInstance().get(Calendar.MONTH));

		switch(month)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				month = 1;
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				
				month = 2;
				break;
			default:
				month = 3;
				break;
		}
		
		String[] arr = new String[(3)*2 + month];
		
		String[] sem = new String[] {
				"Winter","Summer","Fall"
		};
		
		Stack<String> stack = new Stack<String>();
		
		for(int i = year-2; i <= year; i++)
			{
				for(int x = 0; x<((i==year)?month:3); x++)
					{
							
						if(i==year)
							{
								stack.push(sem[x] + " " + i);
							}
						else
							{
								stack.push(sem[x] + " " + i);
							}
					}
			}
				
		
		int l = 0;
		while(!stack.isEmpty())
			{
				arr[l++]=stack.pop();
			}
		return arr;
	}

	private static void GetModified(String url)
	{
		try{
		URL site = new URL(url);

        CheckConnected(site);
        
        URLConnection siteConnection = site.openConnection(); 
       
        String path = lastURL.toString();
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp";
		File file = new File(Working_Directory+File.separator+Res_Directory + File.separator + path);
		
        System.out.println("Connected? " + connected);
       
        if(connected && (siteConnection.getLastModified()!=lastModified || !file.exists()))
	    	System.out.println("MODIFIED.  UPDATING!");
        
        if(connected && (siteConnection.getLastModified()!=lastModified || !site.toString().equals(lastURL) || !file.exists()))
        	{        		
        		lastModified = siteConnection.getLastModified();
        		lastURL = site.toString();
        		
        		WriteTo2(siteConnection, site);
        		
        		if(p!=null)
        			p.setTitle("Home");
        	}  
		}catch(IOException e)
		{
			if(output!=null)
				output.setText("Could not connect to address.\nPlease make sure the exam \nschedule is available online.");
			e.printStackTrace();
		}
	}
	
	private static void WriteTo(HTMLSourceReader reader, URLConnection siteConnection, URL site) throws IOException
	{
		String path = site.getPath();
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp";

		File file = new File(Res_Directory + File.separator + path);
		
		if(!file.exists())
			{
				file.createNewFile();
			}

		if(!filesUsed.contains(file))
			filesUsed.push(file);
		
		InputStream is = siteConnection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
		String str = "";
		while(br.ready())
		{
			str+=br.readLine()+"\n";
		}
		br.close();
		is.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		String s = reader.ParseChunk(str);
		bw.write(s);
		bw.close();
		
	}
	// ReWrite This
	private static void WriteTo(URLConnection siteConnection, URL site) throws IOException
	{
		String path = site.getPath();
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp";

		File file = new File(Res_Directory + File.separator + path);
		
		if(!file.exists())
			{
				file.createNewFile();
			}

		if(!filesUsed.contains(file))
			filesUsed.push(file);
		
		InputStream is = siteConnection.getInputStream();
		
		BufferedReader bufRead = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
		String text = "Last Modified:"+lastModified+"\n";
		BufferedReader sc = null;

		Stack<Character> stack = new Stack<Character>();
		String line = bufRead.readLine();

		BufferedWriter bufWrite = new BufferedWriter(new FileWriter(file));	
		while(line!=null)
			{
				line = bufRead.readLine();
				String temp = "";
				char tomp = '!';
				if(line!=null && line.indexOf("<tr height=\"20\"")>=0)
				{
						temp = bufRead.readLine();

						sc = new BufferedReader(new StringReader(temp));
						char c = ' ';

						boolean canRead = false;
						String etomp = "";
						while(temp.indexOf("</tr>")<0) 
							{
							
							c = (char) sc.read();
							
							try
								{
									Thread.sleep(0);
								} catch (InterruptedException e)
								{
									e.printStackTrace();
								}
							
							if(c == '>') {
								stack.push(c);
								canRead = true;
							}else if(c == '<'&&stack.size()>0)
								{
		
									char ch = stack.pop();
									if(etomp.equals(""))
										{
										continue;
										}
									temp = etomp;
									etomp = "";
									
									//temp = bufRead.readLine();
									if(temp.contains("<strong>"))
									{
										int l = temp.indexOf("<strong>");
										temp = temp.substring(l+"<strong>".length());
										temp = temp.substring(0, temp.indexOf("<"));
									}
									sc = new BufferedReader(new StringReader(temp));

									canRead = false;
									
									text+=",";
									if(temp == null)
										break;
									
								}
							else if(canRead) {
								etomp+=c;	
								
								text+=c;
							}if(c == 65535)
								break;
						}
						bufWrite.append(text+System.getProperty("line.separator"));
						text = "";
					}
			}

		if(sc!=null)
			sc.close();
		
		is.close();
		
		bufRead.close();
		
		//FileConfigurer.SetOwnerWritable(file, true);
			
		bufWrite.close();
		
		//FileConfigurer.SetOwnerWritable(file, false);
		//FileConfigurer.SetOwnerWritable(Settings_File, true);
			
		bufWrite = new BufferedWriter(new FileWriter(Settings_File));

		
		bufWrite.write("Last Modified:"+siteConnection.getLastModified()+"\nLast URL:" + site.toString());
		
		bufWrite.close();
		
		//FileConfigurer.SetOwnerWritable(Settings_File, false);
		
	}
	
	public static void WriteTo2(URLConnection siteConnection, URL site) throws IOException
	{
		String path = site.getPath(); // Get the Path of the URL
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp"; // Parse the Path
		
		// Create a file by the name of the path
		File file = new File(Working_Directory + File.separator + Res_Directory + File.separator + path);
		String s = file.getAbsolutePath();
		if(!file.exists())
			{
				file.createNewFile();
			}

		// Add it to the stack so we can close it properly later
		if(!filesUsed.contains(file))
			filesUsed.push(file);
		
		// Input stream for the site
		InputStream is = siteConnection.getInputStream();
		
		// Use a buffered reader for the input stream
		BufferedReader bufRead = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
		String text = "Last Modified:"+lastModified+"\n"; // Header text
		
		// Buffered Reader for reading the internal text
		BufferedReader sc = null;
		
		// Current line read
		String line = bufRead.readLine();

		BufferedWriter bufWrite = new BufferedWriter(new FileWriter(file));	
		
		// While we have lines to read
		while(line != null)
		{
			// If we have found the start record tag
			if(line.indexOf("<tr height=\"20\"")>=0)
			{
				// Start recording
				line = bufRead.readLine();
				if(line.contains("<span"))
				{
					String lineA = line.substring(0, line.indexOf("<span"));
					String lineB = line.substring(line.indexOf("</span>")+"</span>".length());
					line = lineA+"  "+lineB;
				}
				sc = new BufferedReader(new StringReader(line));
				char c = '!';

				//System.out.println("><|"+(temp));
				boolean canRead = false;
				String runningText = ""; // The text between ">" and "<"
				while(line.indexOf("</tr>")<0) 
					{
						c = (char)sc.read();
						// System.out.println(c + "<< char");
						
						if(c == 65535)
								break;
						
						if(c == '>')
						{
							canRead = true;
						}else
							if(c == '<')
							{
								if(canRead == false)
									continue;
								if(line.contains("<span"))
								{
									runningText+=" ";
									canRead = false;
									continue;
								}
								canRead = false;
								if(!runningText.equals(""))
								{
									text+=",";
									runningText = "";
									line = bufRead.readLine();
									sc = new BufferedReader(new StringReader(line));
									if(line == null)
										break;
									
								}
							}
						else 
							if(canRead) {
								runningText+=c;
								text+=c;
							}
					}
				bufWrite.append(text+System.getProperty("line.separator"));
				text = "";
			}
			line = bufRead.readLine();
			// Read the next line
		}

		bufWrite.close();
	}
	
	private void FindTime(URL site) throws IOException
	{
		GetModified(site.toString());
		String path;
		if(!connected)
			path = ParseSemester(semesterBox.getSelectedItem().toString());
		else path = lastURL.toString();
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp";
		File file = new File(Working_Directory+separator+Res_Directory + File.separator + path);
		System.out.println(file.getAbsolutePath()+" PATH");
		if(!file.exists())
		{
			file.createNewFile();
		}
		
		//FileConfigurer.SetOwnerReadable(file, true);
		
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = br.readLine();
		String text = "";

		int count = 0;

		String spaces = "";
		for(int i = coursesBox.getSelectedItem().toString().length(); i < 4; i++)
		{
			spaces+=" ";
		}
		while(br.ready())
			{
				// Read Next Line
				line = br.readLine();
				// TEST THIS WHEN INTERNET
				
				// Build the text from input boxes
				String ph = coursesBox.getSelectedItem().toString().toUpperCase() +spaces+ number.getText() + ","+section.getText().toUpperCase();
				//System.out.println(ph + " << PH");
				if(line.length()>0 && line.toUpperCase().charAt(0)>ph.toUpperCase().charAt(0))
				{
					System.out.println("past, no dice\n"+line);
					break;
				}
				if(line.startsWith(ph))
				{
					Scanner sc = new Scanner(line);
					while(line.contains(coursesBox.getSelectedItem().toString().toUpperCase()+(spaces) + number.getText() + (section.getText().length()==0?"":","+section.getText().toUpperCase())))
					{

						try {

						sc = new Scanner(line);
						sc.useDelimiter(",");

						count++;
						text += "COURSE: " + coursesBox.getSelectedItem().toString().toUpperCase() + " " +number.getText()+ (section.getText().toUpperCase().equals("")?" ":" ");
						sc.next();

						count++;
						text+=sc.next().toUpperCase();
						
						sc.next();
						
						if(line.contains("NO EXAM") || line.contains("--"))
							{

								count+=2;
								text += "\nDATE: " + "NO EXAM";
								text += "\nTIME: " + "N/A\n";
								break;
							}
						else if((line.contains("?")&&!line.contains("-"))||line.contains("SEE INSTRUCTOR"))
						{
							count+=2;
							text+="\nNo Information";
							text+="\nSee Instructor\n";
							break;
						}
						else
							{
								Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
								String dt= sc.next();
								
								Scanner ec = new Scanner(dt);
								ec.useDelimiter("-");
								
								String day = ec.next();
								String month = ec.next();
								ec.useDelimiter(",");
								String year = ec.next().substring(1);
								
								try
								{
								String input_date=day+"/"+getMonth(month)+"/"+year;
								SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
								Date dt1= format1.parse(input_date);
									
								DateFormat format2=new SimpleDateFormat("EEE"); 
								String finalDay=format2.format(dt1);
								
								count++;
								text += "\nDATE: " + finalDay.toUpperCase() + " " + day + " " + month   + " " + year + " ";

								count++;
								text += "\nTIME: " + sc.next();
								ec.close();
									} catch (ParseException e)
								{
									e.printStackTrace();
								}
							}
						String s = sc.next();
						
						while(s.length()==0||(s.charAt(0)>='0' && s.charAt(0)<='9')) {
							s=sc.next();
						}

						count++;
						text += "\nROOM: " + s;

						count++;
						text += "\nPROF: " + sc.next()+"\n\n";

						}catch(NoSuchElementException e)
						{
							text+=" \nError reading information\n\n";

							count++;
							System.out.println(text);
						}finally
						{
							line = br.readLine();
						}
					}
						sc.close();
					}else if(line.length()>0 && ph.length()>0 && line.charAt(0)>ph.charAt(0))
						break;
			}
		if(text.equals("") && !output.getText().equals("Could not connect to address.\nPlease make sure the exam \nschedule is available online."))
		{
			text = "Course could not be found!";

		}
		output.setText(text);
		output.setPreferredSize(new Dimension(output.getWidth(),output.getLineCount()*out_y-out_y));
		pack();
		br.close();
		
		//FileConfigurer.SetOwnerReadable(file, false);
	}

	String ParseSemester(String s)
	{
		String month = s.substring(0, s.indexOf(" "));
		String year = s.substring(s.indexOf(" ")+1);
		switch(month.toLowerCase())
		{
		case "winter":
			month = "01";
			break;
		case "summer":
			month = "05";
			break;
		case "fall":
			month = "09";
			break;
		}
		return year+month+"-exam-schedule.htm";
	}
	
	private String getMonth(String dt)
	{
		if(dt.toLowerCase().contains("jan"))
			return "01";
		else if(dt.toLowerCase().contains("feb"))
			return "02";
		else if(dt.toLowerCase().contains("mar"))
			return "03";
		else if(dt.toLowerCase().contains("apr"))
			return "04";
		else if(dt.toLowerCase().contains("may"))
			return "05";
		else if(dt.toLowerCase().contains("jun"))
			return "06";
		else if(dt.toLowerCase().contains("jul"))
			return "07";
		else if(dt.toLowerCase().contains("aug"))
			return "08";
		else if(dt.toLowerCase().contains("sep"))
			return "09";
		else if(dt.toLowerCase().contains("oct"))
			return "10";
		else if(dt.toLowerCase().contains("nov"))
			return "11";
		else return "12";
	}

	JPopupMenu pop;
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		switch(arg0.getComponent().getName()) 
		{
			case "output":
				if(arg0.getButton() == 3) {
					pop = new JPopupMenu();
					
					JMenuItem addToSheet = new JMenuItem("Add To Sheet");
						addToSheet.addActionListener(worksheet);
						addToSheet.setForeground(scheme.c_font);
						addToSheet.setBackground(scheme.c_bar);
					JMenuItem openManager = new JMenuItem("Open Manager");
						openManager.addActionListener(worksheet);
						openManager.setForeground(scheme.c_font);
						openManager.setBackground(scheme.c_bar);
					pop.add(addToSheet);
					pop.add(openManager);
					pop.show(arg0.getComponent(),arg0.getX(), arg0.getY());
					// Perform popup
					System.out.println(arg0.getButton());
				}
				break;
				default: 
				if(pop!=null&&pop.isEnabled())
				{
					//pop.setEnabled(false);
				}
		}
		// TODO Auto-generated method stub
		
	}
	
	public static boolean CheckConnected(URL url)
	{
		try
		{
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.connect();

	      	if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
	      	{
	      		connected = true;
	      	}
		} catch (IOException e)
		{
			connected = false;
		}
		return connected;
	}

	public String lastName = "";
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		if(soundOn)
		{
			String name = arg0.getComponent().getName();
			if(!lastName.equals(name)) {
				Thread t = new VocalThread(name);
				t.start();
				lastName = name;
			}
		}
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		Component comp = arg0.getComponent();
		if(comp instanceof JTextArea)
			{
				((JTextArea)comp).setEditable(true);
				((JTextArea)comp).setBackground(Color.WHITE);
				
			}
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		Component comp = arg0.getComponent();
		if(comp.getName().equals("Course Number"))
			{
			if(comp.getBackground().equals(Color.red))
				{
					comp.setBackground(Color.WHITE);
				}
				
		}
		else if(comp instanceof JButton)
		{
			((JButton)comp).doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		if(arg0.getKeyCode()==9)
		if(soundOn)
		voice.speak(this.getFocusOwner().getName());
		
		Thread t;
		if(arg0.getKeyCode() == 16)
			switch(arg0.getComponent().getName())
			{
			case "go!":
			case "section":
			case "course number":
			case "courses box":
			case "semester box":
				t = new VocalThread(arg0.getComponent().getName());
				t.start();
				break;
			case "output":
				if(((JTextArea)arg0.getComponent()).getText().equals(""))
				{
					t = new VocalThread(arg0.getComponent().getName());
					t.start();
				}
				else {
					t = new VocalThread(((JTextArea)arg0.getComponent()).getText());
					t.start();
				}
				break;
			}
		else if(arg0.getKeyCode()==27)
			this.dispose();
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		

	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String name = arg0.getActionCommand();
		switch(name)
		{
			case "Go!":
				if(number.getText().equals(""))
					{
						number.setBackground(Color.RED);
					}
				
				
				try
					{
						FindTime(GetURL());
						
						if(soundOn)
						{
							String t = output.getText();
							Scanner sc = new Scanner(t);
							String te = sc.nextLine();
							if(!t.contains("Course could not be found")) {
								while(te.indexOf("ROOM")!=0 && sc.hasNextLine())
									te = sc.nextLine();
								te = te.substring(te.indexOf(":"));
								BufferedReader br = new BufferedReader(new StringReader(te));
								String s = "";
								char c = ' ';
								while(br.ready() && c != 65535)
									{
										c = (char) br.read();
										s+=c+" ";
									}
								
								t=output.getText().substring(0, output.getText().indexOf("ROOM:")+5)+s+output.getText().substring(output.getText().indexOf("PROF:"));
							}
							Thread th = new VocalThread(t);
							th.start();
							if(number.getText().equals(""));
								{
									Thread tx = new VocalThread("Please input a course number into the number box.");
									tx.start();
								}
						}
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				
				break;
			case "Exit":
				windowsCount = 0;
				dispose();
				break;
			case "Sound - On":
			case "Sound - Off":
				soundOn=!soundOn;
				sound.setText("Sound - " + (soundOn?"On":"Off"));
			default:
				if(windowsCount==1)
					new Parser(name);
				
		}

	}
	
	

	private String[] FindSectionsForNoSection(URL site) throws IOException {
		GetModified(site.toString());
		String path = lastURL.toString();
		path = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))+".edp";
		
		File file = new File(Res_Directory + File.separator + path);
		
		if(!file.exists())
		{
			file.createNewFile();
		}
		
		//FileConfigurer.SetOwnerReadable(file, true);
		
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = br.readLine();
		String text = "";

		int count = 1;
		
		LinkedList<String> link = new LinkedList<String>();
		while(br.ready())
			{

				line = br.readLine();
				String ph = coursesBox.getSelectedItem().toString().toUpperCase()+ (coursesBox.getSelectedItem().toString().length()==3?" ":"") + number.getText() + ","+section.getText().toUpperCase();

				if(line.startsWith(ph))
					{
					Scanner sc = null;
					while(line.startsWith(coursesBox.getSelectedItem().toString().toUpperCase()+(coursesBox.getSelectedItem().toString().length()==3?" ":"") + number.getText()))
					{
						sc = new Scanner(line);
						sc.useDelimiter(",");
						String a = sc.next();
						String b = sc.next();
						line = br.readLine();
						
						text+=a+" "+b+",";
//STAT106,AB6,11146,23-Apr-2018,14:00 - 17:00,AB-ABA-302,SHAUN SUN,,
						
						
					}
						sc.close();
					}
			}
		
		if(text.equals("") && !output.getText().equals("Could not connect to address.\nPlease make sure the exam \nschedule is available online."))
		{
			text = "Course could not be found!";

		}
		//output.setText(text);
		//output.setPreferredSize(new Dimension(output.getWidth(),count+22*(count-1)));
		br.close();
		
		//FileConfigurer.SetOwnerReadable(file, false);
		return text.split(",");
	}

	private void CleanUp()
	{
		System.out.println("Clean Up Started");
		try
			{
				//FileConfigurer.setAll(Settings_File, false);
				while(!filesUsed.isEmpty())
					{
						//FileConfigurer.setAll(filesUsed.pop(),false);
					}
				SettingsWrite();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		
		BufferedWriter bw = null;
		try {
			File file = new File(Hidden_Dir+separator+"dirs.txt");
			if(file.exists())
			{
				bw = new BufferedWriter(new FileWriter(file));
				String s = "";
				for(String c : lastDirectories)
					s+=c+"\n";
				bw.write(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(bw!=null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		System.out.println("Clean Up Ended");
		
		
	}

	private URL GetURL()
	{
		try
			{
				String s = semesterBox.getSelectedItem().toString();
				String sem = s.substring(0, s.indexOf(" "));
				String yr = s.substring(s.indexOf(" ")+1);

				if(sem.startsWith("F"))
					sem = "09";
				else if(sem.startsWith("W"))
					sem = "01";
				else
					sem = "05";
				
				URL url = new URL("https://www.ufv.ca/arfiles/includes/"+yr+sem+"-exam-schedule.htm");
				url.openConnection();
				return url;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		return null;
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		windowsCount--;
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
		windowsCount++;
		
	}
	
	@Override
	public void dispose()
	{
		
		this.setVisible(false);
		if(this.getName().indexOf("Home") == 0) {
			CleanUp();
			System.exit(0);
			super.dispose();
		}else
			{
				super.dispose();
			}
	}
	@Override
	public void focusGained(FocusEvent arg0) {
		Thread t;
		if(soundOn)
			switch(arg0.getComponent().getName().toLowerCase())
			{
			case "go!":
			case "section":
			case "course number":
			case "courses box":
			case "semester box":
				t = new VocalThread(arg0.getComponent().getName());
				t.start();
				break;
			case "output":
				if(((JTextArea)arg0.getComponent()).getText().equals(""))
				{
					t = new VocalThread(arg0.getComponent().getName());
					t.start();
				}
				else {
					t = new VocalThread(((JTextArea)arg0.getComponent()).getText());
					t.start();
				}
				break;
			}
	}
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
