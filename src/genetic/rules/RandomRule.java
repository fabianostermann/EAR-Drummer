package genetic.rules;

import genetic.DrumPattern;
import genetic.Random;
import input.InputAnalysis;

public class RandomRule extends Rule {
	
	public RandomRule() {
		super("Random", "Adds a random value to the fitness");
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//bewertet zufällig, simuliert eigenständigen drummer
		
		float randomRate = Random.nextFloat();
		
		return randomRate;
	}
}