package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Random;

public class RandomPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "random";
	}

	@Override
	public float rate(DrumPattern pattern) {
		return Random.nextFloat();
	}
	
}
