package genetic.combine.solo;

import input.InputAnalysis;

public class WideSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "wide";
	}

	@Override
	public float rate(InputAnalysis analysis) {

		if (analysis.numberOfNotes <= 0) {
			return 0f;
		}
		
		float rangeFactor = (float) Math.min(analysis.keyRange, 36) / 36;
		return rangeFactor;
	}

}
