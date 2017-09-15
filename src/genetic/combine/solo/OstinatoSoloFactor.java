package genetic.combine.solo;

import input.InputAnalysis;

public class OstinatoSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "ostinato";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 0 ||
				analysis.numberOfNotes * 3 < analysis.numberOfDifferentNotes) {
			return 0f;
		}
		
		int numberOfDifferentNotes = Math.max(1, analysis.numberOfDifferentNotes - 3);

		float ostinatoFactor = 1f - ((float)numberOfDifferentNotes / analysis.numberOfNotes);
		return ostinatoFactor;
	}

}
