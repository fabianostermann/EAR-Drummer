package init;

import java.io.PrintStream;

public class Streams {

	//TODO set all streams to debugStream and System.err = errorStream
	//TODO only use these streams for output, make them print only in DEBUG mode by default
	// and then both to debug stream and standard stream
	
	//streams settings
	public static PrintStream debugStream = null;
	public static PrintStream errorStream = null;
	static {
		try {
			errorStream = new SimpleDebugFileStream("error");
//			System.setErr(errorStream);
		} catch (Exception e) {
			errorStream = System.err;
			e.printStackTrace();
		}
		try {
			debugStream = new SimpleDebugFileStream("debug");
		} catch (Exception e) {
			debugStream = System.out;
			e.printStackTrace();
		}
	}
	
	// change debugStream to System.out for debug without --debug mode
	public static final PrintStream evolutionOut = debugStream;
	public static final PrintStream inputAnalysisOut = debugStream;
	public static final PrintStream mutationOut = debugStream;
	public static final PrintStream ruleOut = debugStream;
	public static final PrintStream midiOut = debugStream;
	public static final PrintStream recordOut = System.out;
	
}
