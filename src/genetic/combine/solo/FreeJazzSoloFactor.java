package genetic.combine.solo;

import input.InputAnalysis;

public class FreeJazzSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "freeJazz";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		
		if (analysis.numberOfDifferentNotesOctave <= 7) {
			return 0f;
		}

		float freeJazzFactor = 1f - (1f / analysis.numberOfDifferentNotesOctave) + 0.08f;
		freeJazzFactor = freeJazzFactor * freeJazzFactor;
		return freeJazzFactor;
	}

}
