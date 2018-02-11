package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Random;
import input.InputAnalysis;

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
