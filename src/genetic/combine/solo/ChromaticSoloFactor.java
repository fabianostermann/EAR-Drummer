package genetic.combine.solo;

import input.InputAnalysis;

public class ChromaticSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "chromatic";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 1 ||
				analysis.intervalMin > 1) {
			return 0f;
		}
		
		float chromaticFactor = 1 / Math.max(1f, analysis.intervalAverage);
		return chromaticFactor;
	}

}
