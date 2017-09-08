package genetic.combine.pattern;

import genetic.DrumPattern;

public class EmptyPatternFactor extends PatternFactor {

	public EmptyPatternFactor() {
		super("Empty");
	}

	@Override
	public float rate(DrumPattern pattern) {
		// TODO Empty debug pattern
		return 0;
	}

}
