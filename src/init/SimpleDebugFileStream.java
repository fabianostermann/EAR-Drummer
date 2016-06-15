package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class SimpleDebugFileStream extends PrintStream {

	private File file;
	
	private long maxSize = 10000000; //10MB
	private boolean maxSizeReached = false;
	
	public SimpleDebugFileStream(String name) throws FileNotFoundException {
		super(name+".txt");
		
		this.file = new File(name+".txt");
		
		this.println(name+" stream initialized");
	}
	
	//Use no other method than this to write into file!
	@Override
	public void println(String s) {
		
		if (maxSizeReached)
			return;
		
		super.print(System.currentTimeMillis() + " -- ");
		super.print(s);
		super.println();
		
		if (file.length() > maxSize) {
			maxSizeReached = true;
			super.println("Max file size reached. Restart program to see more debug messages.");
		}
		
	}
	
	
	
}
