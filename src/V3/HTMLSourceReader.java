package v3;

import java.util.Scanner;

public class HTMLSourceReader {

	String[] ignoreTags;
	String[] triggerTags;
	String startTag;
	String endTag;
	String delim; // Add Delimeter to constructors
	boolean startOnOwnLine;
	
	/**
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * This reader assumes no tags will be ignored inside the trigger tags, and there are no tags to indicate when to read and when to stop.  The delimiter is set to the default string "##".
	 */
	public HTMLSourceReader(String[] triggerTags)
	{
		this(triggerTags,null,null,null,true, "##");
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * This reader assumes there are no tags to indicate when to read and when to stop.  The delimiter is set to the default string "##"
	 */
	public HTMLSourceReader(String[] triggerTags,String[] ignoreTags)
	{
		this(triggerTags,ignoreTags,null, null,true);
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * @param delim : The delimiter used on output.  As it parses each tag line, it uses this to separate.
	 * This reader assumes there are no tags to indicate when to read and when to stop.
	 */
	public HTMLSourceReader(String[] triggerTags,String[] ignoreTags, String delim)
	{
		this(triggerTags,ignoreTags,null, null,true, delim);
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * @param startTag : This speeds up the reader as it only begins searching for tags after a specific tag.  This is especially useful if there are many trigger tags, for a large html source.
	 * @param endTag : This indicates when to stop reading.  This tag is often the same as the start tag, only with '\' after the first '<'.
	 * This assumes that there is a new line immediately after the start tag is found, or that there is no text to be found inside the tag after the start tag and the delimiter is set to the default string "##".
	 * @see HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag, boolean startOnOwnLine)
	 */
	
	public HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag)
	{
		this(triggerTags,ignoreTags, startTag, endTag,true, "##");		
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * @param startTag : This speeds up the reader as it only begins searching for tags after a specific tag.  This is especially useful if there are many trigger tags, for a large html source.
	 * @param endTag : This indicates when to stop reading.  This tag is often the same as the start tag, only with '\' after the first '<'.
	 * @param delim : The delimiter used on output.  As it parses each tag line, it uses this to separate.
	 * This assumes that there is a new line immediately after the start tag is found, or that there is no text to be found inside the tag after the start tag.
	 * @see HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag, boolean startOnOwnLine)
	 */
	
	public HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag, String delim)
	{
		this(triggerTags,ignoreTags, startTag, endTag, true, delim);		
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * @param startTag : This speeds up the reader as it only begins searching for tags after a specific tag.  This is especially useful if there are many trigger tags, for a large html source.
	 * @param endTag : This indicates when to stop reading.  This tag is often the same as the start tag, only with '\' after the first '<'.
	 * @param startOnOwnLine : This indicates whether or not the startTag is on its own line, or no text in the start tag is to be read.  Setting this to false will read the text in the tag.  Note: this tag should also be included in the trigger tags.
	 * This sets the delimiter to the default string "##".
	 */
	public HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag, boolean startOnOwnLine)
	{
		this.ignoreTags = ignoreTags;
		this.triggerTags = triggerTags;
		this.startTag= startTag;
		this.endTag = endTag;
		this.startOnOwnLine = startOnOwnLine;
	}
	
	/**
	 * 
	 * @param triggerTags : The html tags that trigger the start of parsing
	 * @param ignoreTags : The html tags to ignore after a trigger tag is found
	 * @param startTag : This speeds up the reader as it only begins searching for tags after a specific tag.  This is especially useful if there are many trigger tags, for a large html source.
	 * @param endTag : This indicates when to stop reading.  This tag is often the same as the start tag, only with '\' after the first '<'.
	 * @param startOnOwnLine : This indicates whether or not the startTag is on its own line, or no text in the start tag is to be read.  Setting this to false will read the text in the tag.  Note: this tag should also be included in the trigger tags.
	 * @param delim : This sets the delimiter string used for separating the parsed results, which are returned in the form of [TOKEN][delim][TOKEN][delim]
	 */
	public HTMLSourceReader(String[] triggerTags, String[] ignoreTags, String startTag, String endTag, boolean startOnOwnLine, String delim)
	{
		this.ignoreTags = ignoreTags;
		this.triggerTags = triggerTags;
		this.startTag= startTag;
		this.endTag = endTag;
		this.startOnOwnLine = startOnOwnLine;
		this.delim = delim;
	}
	
	public String ParseChunk(String chunk)
	{
		Scanner sc = new Scanner(chunk);
		String s = "";
		if(startTag!=null)
		{
			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				
				// Find StartTag
				if(line.contains(startTag))
				{

					while(!line.contains(endTag)) 
					{
						System.out.println("0");
						
						System.out.println("1"+line);
							
						for(String t:triggerTags) 
						{
							System.out.println("2"+t);
							if(line.contains(t))
							{
								s+=ParseLine(line)+delim;
								break;
							}
						}
							
						line = sc.nextLine();
					}
				}
			}
		}else
		{
			while(sc.hasNextLine())
			{
				s+=ParseLine(sc.nextLine());
			}
		}
		sc.close();
		return s;
	}
	
	public String ParseLine(String line)
	{

		System.out.println(line+"<<1");
		int max = 0;
		int maxLength = 0;
		// Find the last index of the ignore tags
		line = line.substring(line.indexOf('>')+1);
		line = line.substring(0, line.lastIndexOf('<'));
		System.out.println(line+"<<2");
		for(String t : ignoreTags)
		{
			int x = line.indexOf(t);
			if(x<0)
				continue;
			else System.out.println(t);
			if(x>=max) {
				max = x;
				maxLength = t.length();
			}
		}
		System.out.println("MAX: "+max +"length"+maxLength);
		String parse = line.substring(max+maxLength);
		int pind = parse.indexOf('<');
		if(pind >= 0)
			parse = parse.substring(0,pind);
		System.out.println(parse+"<<3");
		return parse+"\n";
	}
	
	/**
	 * 
	 * @param text : The string to be parsed through the delimiter defined by the constructor.
	 * @return : The array containing all parsed tokens.
	 */
	public String[] split(String text)
	{
		
		return ignoreTags;
	}
}
