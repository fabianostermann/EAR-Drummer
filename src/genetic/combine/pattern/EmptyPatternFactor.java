package genetic.combine.pattern;

import input.InputAnalysis;
import genetic.DrumPattern;

public class EmptyPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "empty";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		return Float.NaN;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
