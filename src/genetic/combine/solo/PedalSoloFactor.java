package genetic.combine.solo;

import input.InputAnalysis;

public class PedalSoloFactor extends SoloFactor {

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 0) {
			return 0f;
		}
		
		float pedalFactor = Math.min(5000f, (float)analysis.lengthAverage) / 5000f;

		return pedalFactor;
	}

	@Override
	public String getName() {
		return "pedal";
	}

}
