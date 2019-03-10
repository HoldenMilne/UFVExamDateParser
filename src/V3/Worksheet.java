package v3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Worksheet implements ActionListener{
	WorksheetViewer viewer;
	ArrayList<WorksheetEntry> entries;
	
	public Worksheet()
	{
		entries = new ArrayList<WorksheetEntry>();
	}
	
	public void Add(WorksheetEntry e)
	{
		entries.add(e);
	}
	
	public void Add(String courseName,String date,String time,String room,String prof)
	{
		entries.add(new WorksheetEntry(courseName,date,time,room,prof));
	}
	
	public void Remove(String courseName)
	{
		for(WorksheetEntry e : entries)
		{
			if(e.className.equalsIgnoreCase(courseName))
			{
				entries.remove(e);
				return;
			}
		}
	}
	
	public void RemoveAll(String courseName)
	{
		for(WorksheetEntry e : entries)
		{
			if(e.className.equalsIgnoreCase(courseName))
			{
				entries.remove(e);
			}
		}
	}
	
	public void DisplayAll()
	{
		StringBuilder s = new StringBuilder();
		for(WorksheetEntry e : entries)
		{
			s.append(e.className);
			s.append(e.date);
			s.append(e.time);
			s.append(e.room);
			s.append(e.prof);
		}
		
		// temp
		System.out.println(s);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
