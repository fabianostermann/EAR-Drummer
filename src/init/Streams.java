package init;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Streams {

	//TODO set all streams to debugStream and System.err = errorStream
	
	//streams settings
	public static PrintStream debugStream = null;
	private static PrintStream errorStream = null;
	private static PrintStream nullStream = new PrintStream(new OutputStream()
	{ @Override public void write(int b) throws IOException {} });
	
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
	public static final PrintStream evolutionOut = nullStream;
	public static final PrintStream inputAnalysisOut = nullStream;
	public static final PrintStream mutationOut = nullStream;
	public static final PrintStream ruleOut = debugStream;
	public static final PrintStream midiOut = nullStream;
	public static final PrintStream recordOut = nullStream;
	public static final PrintStream CombiOut = debugStream;
	
}
