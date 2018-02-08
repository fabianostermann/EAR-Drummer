package bass;

public class ChordLib {
	
	public static final String[] sharpRoots = new String[] {
			"bis", "cis", "d", "dis", "e", "eis", "fis", "g", "gis", "a", "ais", "b"
	};
	public static final String[] flatRoots = new String[] {
			"c", "des", "d", "es", "fes", "f", "ges", "g", "as", "a", "bes", "ces"
	};
	
	
	public static int rootToInt(String rootName) {
		rootName = rootName.toLowerCase();
		for (int i=0; i<sharpRoots.length; i++)
			if (sharpRoots[i].equals(rootName))
				return i;
		for (int i=0; i<flatRoots.length; i++)
			if (flatRoots[i].equals(rootName))
				return i;
		throw new IllegalArgumentException("Unknown root name: " +rootName);
	}

	public static int[] symbolToIntArray(String chordSymbol) {
		switch (chordSymbol) {
		
		// arrays are ordered by chord options: root - third - fifth - seventh - ...
		case "maj7": return new int[] { 0, 4, 7, 11};
		case "m7": return new int[] { 0, 3, 7, 10};
		case "7": return new int[] { 0, 4, 7, 10};
		case "m7b5": return new int[] { 0, 3, 6, 10};
		case "dim": return new int[] { 0, 3, 6, 9};
		
		default: throw new IllegalArgumentException("Unknown chord symbol: " +chordSymbol);
		}
		
	}

	/**
	 * Syntax elements
	 */
	public static final String PERCENT_REPEAT_SYMBOL = "%";
	public static final String CHORD_SYMBOL_SPLITTER = ":";
	public static final String BAR_LINE_SYMBOL = "|";
	public static final String COMMENT_SYMBOL = "#";
	/**
	 * 
	 * @param code
	 * A String that is written in a chord sheet specific markup language.
	 * @return 
	 * An ChordTable object that hold information about chordNames, chordRoots and chordNotes as arrays
	 */
	public static ChordTable interpretChordTableString(String code) throws IllegalArgumentException {
		ChordTable table = new ChordLib.ChordTable();
		
		// remove comment lines
		while (code.contains(COMMENT_SYMBOL)) {
			int firstCommentSymbolPos = code.indexOf(COMMENT_SYMBOL);
			int followingLineBreak = code.indexOf("\n", firstCommentSymbolPos);
			String tailString = "";
			if (followingLineBreak != -1)
				tailString = code.substring(followingLineBreak+1);
			code = code.substring(0, firstCommentSymbolPos) + tailString;
		}
		
		// remove all kind of spaces and newlines
		code = code.replaceAll("\\s","");
		code = code.replaceAll("\n","");
		while (code.contains(BAR_LINE_SYMBOL+BAR_LINE_SYMBOL))
			code = code.replaceAll("\\"+BAR_LINE_SYMBOL+"\\"+BAR_LINE_SYMBOL, BAR_LINE_SYMBOL);
		 
		if (code.isEmpty())
			return table;

		if (!code.startsWith(BAR_LINE_SYMBOL)) {
			throw new IllegalArgumentException("code must start with '"+BAR_LINE_SYMBOL+"'");
		}//else, remove first barline from code
		code = code.substring(1);
		
		String[] chordElements = code.split("\\"+BAR_LINE_SYMBOL);
		table.chordNames = new String[chordElements.length];
		table.chordRoots = new int[chordElements.length];
		table.chordNotes = new int[chordElements.length][];
		
		// process the chord elements
		for (int i = 0; i < chordElements.length; i++) {
			
			table.chordNames[i] = chordElements[i];
			
			if (chordElements[i].equals(PERCENT_REPEAT_SYMBOL)) {
				if (i <= 0)
					throw new IllegalArgumentException("percent symbol '"+PERCENT_REPEAT_SYMBOL+"' not allowed at beginning.");
				chordElements[i] = chordElements[i-1];
			}
		
			String[] splittedChord = chordElements[i].split(CHORD_SYMBOL_SPLITTER);
			if (splittedChord.length != 2) 
				throw new IllegalArgumentException("chord must have exactly one '"+CHORD_SYMBOL_SPLITTER+"' ('"+chordElements[i]+"':"+i+")");
			
			int root;
			int[] symbol;
			root = ChordLib.rootToInt(splittedChord[0]);
			symbol = ChordLib.symbolToIntArray(splittedChord[1]);
			
			table.chordRoots[i] = root;
			table.chordNotes[i] = symbol;
		}
		
		return table;
	}
	
	public static class ChordTable {
		public String[] chordNames = new String[0];
		public int[] chordRoots = new int[0];
		public int[][] chordNotes = new int[0][0];
		@Override
		public String toString() {
			String chordNamesStr = "";
			String chordRootsStr = "";
			String chordNotesStr = "";
			for (int i = 0; i < chordNames.length; i++) {
				chordNamesStr += chordNames[i]+",";
				chordRootsStr += chordRoots[i]+",";
				chordNotesStr += "(";
				for (int j = 0; j < chordNotes[i].length; j++)
					chordNotesStr += chordNotes[i][j]+",";
				chordNotesStr += ")";
			}
			return "["+"("+chordNamesStr+")" +"; " + "(" + chordRootsStr + ")" + "; " + "{" + chordNotesStr +"}"+"]";
		}
	}

}
