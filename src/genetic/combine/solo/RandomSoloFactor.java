package genetic.combine.solo;

import genetic.Random;
import input.InputAnalysis;

public class RandomSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "random";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		return Random.nextFloat();
	}
	
}
