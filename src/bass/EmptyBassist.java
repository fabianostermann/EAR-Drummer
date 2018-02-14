package bass;

import bass.ChordLib.ChordTable;

public class EmptyBassist implements SimpleBassist {

	@Override
	public String toString() {
		return "Empty";
	}

	@Override
	public Integer nextNote(int barCount, int currTick, int numOfTicks,
			ChordTable table) {
		// Auto-generated method stub
		return null;
	}

}
