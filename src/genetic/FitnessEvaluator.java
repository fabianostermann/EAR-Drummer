package genetic;

import input.InputAnalysis;

public interface FitnessEvaluator {

	public float getFitness(DrumPattern pattern, InputAnalysis analysis);
	
}
