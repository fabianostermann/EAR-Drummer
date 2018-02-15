package bass;

import genetic.Random;
import output.GM1;
import bass.ChordLib.ChordTable;

public class WalkIn4Bassist implements SimpleBassist {

	@Override
	public String toString() { return "Walk in 4"; }

	public static final int HIGHEST_NOTE = GM1.G_1;
	public static final int LOWEST_NOTE = GM1.F_2;
	
	int chromaticApproach = 0;

	int lastMidi = LOWEST_NOTE;
	
	private final int SAVE_RECURSION_DEPTH = 8;
	int recursionLevel = 0;
	@Override
	public Integer nextNote(int barCount, int currTick, int numOfTicks,
			ChordTable table) {
		
		int midi = -1;
		
		if (currTick  == 0) {
			midi = 120+table.chordRoots[barCount];
			chromaticApproach = 0;
		} else if (currTick  == 2) {
			if (Random.nextBoolean(0.1f)) {
				chromaticApproach = Random.nextBoolean(0.67f) ? -1 : 1;
				midi = 120+table.chordRoots[(barCount+1) % table.chordRoots.length]
						+ chromaticApproach*3;
			} else
			midi = 120+table.chordRoots[barCount]
					+ table.chordNotes[barCount][(Random.nextBoolean(0.67f) ? 1 : 3)];
		} else if (currTick  == 4) {
			if (chromaticApproach != 0 || Random.nextBoolean(0.2f)) {
				if (chromaticApproach == 0)
					chromaticApproach = Random.nextBoolean(0.67f) ? -1 : 1;
				midi = 120+table.chordRoots[(barCount+1) % table.chordRoots.length]
						+ chromaticApproach*2;
			} else
				midi = 120+table.chordRoots[barCount] + table.chordNotes[barCount][2];
		} else if (currTick  == 6) {
			if (chromaticApproach == 0)
				chromaticApproach = Random.nextBoolean(0.67f) ? -1 : 1;
			midi = 120+table.chordRoots[(barCount+1) % table.chordRoots.length]
					+ chromaticApproach;
		} else {
			return null;
		}
		
		// same octave as last note
		while (Math.abs(midi - lastMidi) > Math.abs(midi-12 - lastMidi))
				midi -= 12;
		
		// correct range
		if (currTick == 2)
		while (midi > HIGHEST_NOTE)
			midi -= 12;
		if (currTick == 2)
			while (midi < LOWEST_NOTE)
				midi += 12;
		
		if (midi == lastMidi) {
			if (recursionLevel++ > SAVE_RECURSION_DEPTH) {
				System.err.println("Reached save recursion depth ("+SAVE_RECURSION_DEPTH+") of 'WalkIn4' Bassist.");
			} else {
				midi = nextNote(barCount, currTick, numOfTicks, table);				
			}
		}
			
		lastMidi = midi;
		return midi;
	}
}
