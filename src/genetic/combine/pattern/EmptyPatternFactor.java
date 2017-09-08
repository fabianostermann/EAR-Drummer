package genetic.combine.pattern;

import genetic.DrumPattern;

public class EmptyPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "empty";
	}

	@Override
	public float rate(DrumPattern pattern) {
		return Float.NaN;
	}

}
