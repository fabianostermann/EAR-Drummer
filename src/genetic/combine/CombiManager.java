package genetic.combine;

import genetic.DrumPattern;
import genetic.FitnessEvaluator;
import genetic.combine.pattern.EmptyPatternFactor;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.pattern.RandomPatternFactor;
import genetic.combine.solo.EmptySoloFactor;
import genetic.combine.solo.RandomSoloFactor;
import genetic.combine.solo.SoloFactor;
import init.Streams;
import input.InputAnalysis;

import java.util.ArrayList;

public class CombiManager implements FitnessEvaluator {

	private ArrayList<Combi> combis = new ArrayList<Combi>();
	
	private final PatternFactor[] patternFactors = {
			new EmptyPatternFactor(),
			new RandomPatternFactor()
	};
	private final SoloFactor[] soloFactors = {
			new EmptySoloFactor(),
			new RandomSoloFactor()
	};
	
	public CombiManager() {
		initCombinations();
	}

	private void initCombinations() {
		
		combis.add(new Combi(patternFactors[0], soloFactors[0]));
		combis.add(new Combi(patternFactors[1], soloFactors[1]));
		
		Streams.combiOut.println("Initiated combis with " + combis);
	}
	
	public ArrayList<Combi> getList() {
		return this.combis;
	}
	
	public PatternFactor[] getPatternFactors() {
		return this.patternFactors;
	}
	
	public SoloFactor[] getSoloFactors() {
		return this.soloFactors;
	}

	@Override
	public float getFitness(DrumPattern pattern, InputAnalysis analysis) {
		
		float fitness = 0f;
		
		for (Combi combi : combis) {
			fitness += combi.getWeightedFitness(pattern, analysis);
		}
		
		return fitness;
	}
}
