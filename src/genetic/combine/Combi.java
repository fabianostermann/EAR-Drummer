package genetic.combine;

import genetic.DrumPattern;
import genetic.combine.pattern.EmptyPatternFactor;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.solo.EmptySoloFactor;
import genetic.combine.solo.SoloFactor;
import init.Streams;
import input.InputAnalysis;

import java.util.Observable;

public class Combi extends Observable {

	public PatternFactor patternFactor = null;
	public SoloFactor soloFactor = null;
	
	public Combi() {}
	
	public Combi(PatternFactor patternFactor, SoloFactor soloFactor) {
		this.patternFactor = patternFactor;
		this.soloFactor = soloFactor;
	}
	
	private float weight = 1f;
	
	public void setWeight(float weight) {
		weight = Math.max(-1f, weight);
		weight = Math.min(1f, weight);
		
		this.weight = weight;
	}
	
	public float getWeight() {
		return this.weight;
	}
	
	public float getWeightedFitness(DrumPattern pattern, InputAnalysis analysis) {
		
		float rating = 0;
		int numOfFactors = 0;
		float patternRating = Float.NaN, soloRating = Float.NaN;
		
		if (patternFactor != null && patternFactor.getClass() != EmptyPatternFactor.class) {
			patternRating = patternFactor.rate(pattern);
			rating += patternRating;
			numOfFactors++;
		}
		if (soloFactor != null && soloFactor.getClass() != EmptySoloFactor.class) {
			soloRating = soloFactor.rate(analysis);
			rating += soloRating;
			numOfFactors++;
		}
		
		float fitness = (numOfFactors <= 0) ? 0 : (rating / numOfFactors);
		
		setChanged();
		notifyObservers(fitness);
		
		Streams.combiOut.println("Combi - SoloFactor(" + soloFactor.getName() + ")=" + soloRating + ", " + 
				"PatternFactor(" + patternFactor.getName() + ")=" + patternRating + 
				", fitness=" + fitness);
		
		return fitness;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(patternFactor=" + patternFactor + "," + "soloFactor=" + soloFactor + ")";
	}
}
