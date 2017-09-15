package genetic.combine.solo;

import input.InputAnalysis;

public class HoldsworthSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "holdsworth";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 1 ||
				analysis.intervalAverage < 5) {
			return 0f;
		}
		
		float averageDistance = analysis.intervalAverage / 12;
		return averageDistance;
	}

}
