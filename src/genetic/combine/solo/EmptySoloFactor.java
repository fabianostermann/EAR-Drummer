package genetic.combine.solo;

import input.InputAnalysis;

public class EmptySoloFactor extends SoloFactor {

	public EmptySoloFactor() {
		super("Empty");
	}

	@Override
	public float rate(InputAnalysis analysis) {
		// TODO Empty debug pattern
		return 0;
	}

}
