package genetic.combine.solo;

import input.InputAnalysis;

public class LegatoSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "legato";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 1) {
			return 0f;
		}
		
		float gapAverage = (float) Math.max(1f, analysis.gapAverage);
		float legatoFactor = (float) (1d / Math.sqrt(gapAverage));
		return legatoFactor;
	}

}
