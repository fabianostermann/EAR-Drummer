package genetic.combine.solo;

import input.InputAnalysis;

public class VirtuosoSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "virtuoso";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 0) {
			return 0f;
		}

		float virtuosoFactor = 1f - (1f / analysis.numberOfNotes);
		virtuosoFactor = virtuosoFactor * virtuosoFactor * virtuosoFactor * virtuosoFactor;
		return virtuosoFactor;
	}

}
