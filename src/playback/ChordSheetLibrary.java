package playback;

public class ChordSheetLibrary {

	public static final String[] sharpRoots = new String[] {
			"bis", "cis", "d", "dis", "e", "eis", "fis", "g", "gis", "a", "ais", "b"
	};
	public static final String[] flatRoots = new String[] {
			"c", "des", "d", "es", "fes", "f", "ges", "g", "as", "a", "bes", "ces"
	};
	
	
	public static int rootToInt(String rootName) {
		System.out.println(rootName);
		for (int i=0; i<sharpRoots.length; i++)
			if (sharpRoots[i].equals(rootName))
				return i;
		for (int i=0; i<flatRoots.length; i++)
			if (flatRoots[i].equals(rootName))
				return i;
		throw new IllegalArgumentException("Unknown root name: " +rootName);
	}

	public static int[] structureToArray(String chordSymbol) {
		switch (chordSymbol) {
		
		case "maj7": return new int[] { 0, 4, 7, 11};
		case "m7": return new int[] { 0, 3, 7, 10};
		case "7": return new int[] { 0, 4, 7, 10};
		case "m7b5": return new int[] { 0, 3, 6, 10};
		case "dim": return new int[] { 0, 3, 6, 9};
		
		default: throw new IllegalArgumentException("Unknown chord symbol: " +chordSymbol);
		}
		
	}


}
