package genetic.combine.solo;

import input.InputAnalysis;

public class EmptySoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "empty";
	}
	
	@Override
	public float rate(InputAnalysis analysis) {
		return Float.NaN;
	}

	@Override
	public String toString() {
		return "";
	}
}
