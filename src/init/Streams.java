package init;

import java.io.PrintStream;

public class Streams {

	//TODO set all streams to debugStream and System.err = errorStream
	
	//streams settings
	public static PrintStream debugStream = null;
	public static PrintStream errorStream = null;
	static {
		try {
			errorStream = new SimpleDebugFileStream("error", System.err);
			System.setErr(errorStream);
		} catch (Exception e) {
			errorStream = System.err;
			e.printStackTrace();
		}
		try {
			debugStream = new SimpleDebugFileStream("debug", System.out);
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
	public static final PrintStream recordOut = debugStream;
	public static final PrintStream CombiOut = System.out;
	
}
