package bass;

import output.GM1;
import bass.ChordLib.ChordTable;

public class WalkIn4Bassist implements SimpleBassist {

	@Override
	public String toString() {
		return "Walk in 4";
	}

	@Override
	public Integer nextNote(int barCount, int currTick, int numOfTicks,
			ChordTable table) {
		
		// TODO implement bass walk in 4
		if (currTick % 2 == 0)
			return table.chordRoots[barCount];
		else
			return null;
	}

}
