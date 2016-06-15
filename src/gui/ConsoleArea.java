package gui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

@SuppressWarnings("serial")
public class ConsoleArea extends JTextPane {
	
	static final SimpleAttributeSet LOG_STYLE = new SimpleAttributeSet();
	static final SimpleAttributeSet ERROR_STYLE = new SimpleAttributeSet();
	
	static {
		StyleConstants.setForeground(LOG_STYLE, Color.BLACK);
		StyleConstants.setForeground(ERROR_STYLE, Color.RED);
	}	
	
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");
	
	public ConsoleArea() {
		super();

	}
	
	private void append(String text, AttributeSet style) {
		try {
			this.getDocument().insertString(this.getDocument().getLength(), text, style);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void log(String text) {
		
		this.append("\n"+timeFormat.format(Calendar.getInstance().getTime())+" -- ", ConsoleArea.LOG_STYLE);
		this.append(text, ConsoleArea.LOG_STYLE);
		
	}
	
	public void error(String text) {
		
		this.append("\n"+timeFormat.format(Calendar.getInstance().getTime())+" -- ", ConsoleArea.ERROR_STYLE);
		this.append(text, ConsoleArea.ERROR_STYLE);
		
	}
}
