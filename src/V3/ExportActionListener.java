package v3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportActionListener implements ActionListener {
// TODO: MAke a v3 and add an exam class to store the date, the time, etc.
// then make a linked list for storing the added exams.  On add
// find the first occurence that comes after the date.  If the same, check
// to make sure they're not the same class.  If so overwrite
// else add into the list like so: n-1 -> n => n-1 -> p -> n where n
// is whatever we're checking now
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand();
		System.out.println(name);
		switch(name.toLowerCase())
		{
			case "add":
				break;
			case "remove":
				break;
			case "export":
				break;
			case "view":
				break;
		}

	}

}
