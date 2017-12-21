package playback;

import java.util.ArrayList;

public class ChordSheetManager {

	public static final String INTERPRETING_SUCCESSFUL = "interpreting_successful";
	public static final String LINE_BREAK = "<LINEBREAK>";
	public static final String PERCENT_REPEAT = "%";
	
	public String interpret(String code) {
		
		code = code.replaceAll("\n",LINE_BREAK);
		code = code.replaceAll("\\s","");

		if (!code.startsWith("|"))
			return "Chords must start with a barline \"|\"";
		if (!code.endsWith("|"))
			return "Chords must end with a barline \"|\"";
		
		ArrayList<ChordStruct> chordList = new ArrayList<>();
		
		String[] chords = code.split("\\|");
		for (int i = 1; i < chords.length; i++) {
			if (chords[i].equals(LINE_BREAK))
				continue;
			if (chords[i].equals(PERCENT_REPEAT))
				chords[i] = chords[i-1];
			
			String[] splittedChord = chords[i].split(":");
			if (splittedChord.length != 2) 
				return "chord must have one \":\": "+chords[i];
			
			int root;
			int[] structure;
			try {
				root = ChordSheetLibrary.rootToInt(splittedChord[0]);
			} catch (IllegalArgumentException e) {
				return e.getMessage();
			}
			try {
				structure = ChordSheetLibrary.structureToArray(splittedChord[1]);
			} catch (IllegalArgumentException e) {
				return e.getMessage();
			}
			chordList.add(new ChordStruct(root, structure));
		}
		
		return chordList.toString();
		
//		return INTERPRETING_SUCCESSFUL;
	}
	
	public class ChordStruct {
		
		private int root;
		private int[] structure;
		
		public ChordStruct(int root, int[] structure) {
			if (root < 0 || root > 11)
				throw new IllegalArgumentException("root must in interval [0,11]: "+root);
			if (structure == null)
				throw new IllegalArgumentException("structure is null");
			if (structure.length == 0)
				throw new IllegalArgumentException("structure is empty");
			this.root = root;
			this.structure = structure;
		}
	
		public int getRoot() { return root; }
		public int[] getStructure() { return structure; }
		
		@Override
		public String toString() {
			String structureString = "";
			for (int i=0; i<structure.length; i++)
				structureString += structure[i]+"-";
			if (structureString.endsWith("-"))
				structureString = structureString.substring(0, structureString.length()-1);
			return "("+root+"|"+structureString+")";
		}
	}
}
