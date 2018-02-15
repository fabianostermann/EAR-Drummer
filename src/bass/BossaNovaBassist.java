package bass;

import output.GM1;

public class BossaNovaBassist implements SimpleBassist {

	public String toString() { return "Bossa Nova"; }
	
	public static final int HIGHEST_NOTE = GM1.Gb_1;
	
	public Integer nextNote(int barCount, int currTick, int numOfTicks, ChordLib.ChordTable table) {
		
		int midi = -1;
		
		// Bossa Nova
		if (currTick == 0) {
			 // the one of the bar
			midi = 120+table.chordRoots[barCount];
		} else if (currTick == 3) {
			midi = 120+table.chordRoots[barCount] +table.chordNotes[barCount][2];
		} else if (currTick == 4) {
			// the middleOfTheBar
			midi = 120+table.chordRoots[barCount] +table.chordNotes[barCount][2];
		} else if (currTick == numOfTicks-1) {
			// before next one
			midi = 120+table.chordRoots[(barCount+1) % table.chordRoots.length];
		} else {
			return null;
		}
		
		while (midi > HIGHEST_NOTE)
			midi -= 12;
		
		return midi;
	}
}
