package genetic.combine.pattern;

import input.InputAnalysis;
import genetic.DrumPattern;
import genetic.Random;

public class RandomPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "random";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		return Random.nextFloat();
	}
	
}
