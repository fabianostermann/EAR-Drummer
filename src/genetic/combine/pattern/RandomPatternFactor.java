package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Random;

public class RandomPatternFactor extends PatternFactor {

	public RandomPatternFactor() {
		super("Random");
	}

	@Override
	public float rate(DrumPattern pattern) {
		return Random.nextFloat();
	}
	
}
