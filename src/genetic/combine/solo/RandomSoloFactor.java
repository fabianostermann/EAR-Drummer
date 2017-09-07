package genetic.combine.solo;

import genetic.Random;
import input.InputAnalysis;

public class RandomSoloFactor extends SoloFactor {

	public RandomSoloFactor() {
		super("Random");
	}

	@Override
	public float rate(InputAnalysis analysis) {
		return Random.nextFloat();
	}
	
}
