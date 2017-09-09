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
	
	public Combi() {
		this(new EmptyPatternFactor(), new EmptySoloFactor());
	}
	
	public Combi(PatternFactor patternFactor, SoloFactor soloFactor) {
		this.patternFactor = patternFactor;
		this.soloFactor = soloFactor;
	}
	
	private float weight = 1f;
	
	public void setWeight(float weight) {
		if (weight > 1f || weight < 0f)
			System.err.println(this + " clips weight " + weight + " to [0,1]");
		weight = Math.max(0f, weight);
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
		float weightedFitness = fitness * this.weight;
		
		setChanged();
		notifyObservers(weightedFitness);
		
		Streams.combiOut.println(super.toString() + "->SoloFactor(" + soloFactor.getName() + ")=" + soloRating + ", " + 
				"PatternFactor(" + patternFactor.getName() + ")=" + patternRating + 
				", weight=" + weight + ", fitness=" + fitness + ", weightedFitness=" + weightedFitness);
		
		return weightedFitness;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + "soloFactor=" + soloFactor + "," + "patternFactor=" + patternFactor + ")";
	}
}
