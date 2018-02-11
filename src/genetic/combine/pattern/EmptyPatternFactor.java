package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

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
