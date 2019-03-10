package v3;

public class WorksheetEntry {

	String className;
	String date; 
	String time;
	String room;
	String prof;
	
	public WorksheetEntry()
	{
		this("","","","","");
	}
	
	public WorksheetEntry(String className)
	{
		this(className,"","See Prof","","");
	}
	
	public WorksheetEntry(String className,String prof)
	{
		this(className,"","See Prof","",prof);
	}

	public WorksheetEntry(String className, String date, String time, String room, String prof) {
		this.className = className;
		this.date = date;
		this.time = time;
		this.room = room;
		this.prof = prof;
	}
	
}
