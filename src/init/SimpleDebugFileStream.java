package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;

public class SimpleDebugFileStream extends PrintStream {

	private File file;
	
	private long maxSize = 10000000; //10MB
	private boolean maxSizeReached = false;
	
	private Calendar calendar = Calendar.getInstance();
	
	private PrintStream[] additionalStreams;
	
	private boolean isClosed = false;
	
	private final static String LOG_FILE_SUFFIX = ".log";
	
	public SimpleDebugFileStream(String name, PrintStream...streams ) throws FileNotFoundException {
		super(name+LOG_FILE_SUFFIX);
		
		this.file = new File(name+LOG_FILE_SUFFIX);
		
		this.additionalStreams = streams;
		
		this.println(name+" stream initialized");
	}
	
	/**
	 * Use no other method than this to write into file!
	 */
	@Override
	public void println(String s) {
		this.print(s+"\n");
	}
	
	@Override
	public void print(String s) {
		
		if (isClosed)
			return;
		
		for (PrintStream stream : additionalStreams) {
			stream.print(s);
		}
		
		if (maxSizeReached)
			return;
		
//		super.println(System.currentTimeMillis() + " -- " + s);
		super.print(Calendar.getInstance().get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND) + "."
				+ calendar.get(Calendar.MILLISECOND) + " -- "
				+ s);
		
		if (file.length() > maxSize) {
			maxSizeReached = true;
			super.println("Max file size reached. Restart program to see more debug messages.");
		}
		
	}
	
	@Override
	public void close() {
		isClosed = true;
		super.close();
	}
	
}
