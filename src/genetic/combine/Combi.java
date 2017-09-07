package genetic.combine;

import genetic.DrumPattern;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.solo.SoloFactor;
import input.InputAnalysis;

import java.util.Observable;

public class Combi extends Observable {

	public static final int LIMIT = 1000;

	public PatternFactor patternFactor;
	public SoloFactor soloFactor;
	
	private float weight = 1f;
	
	public void setWeight(float weight) {
		weight = Math.max(-1f, weight);
		weight = Math.min(1f, weight);
		
		this.weight = weight;
	}
	
	public float getWeight() {
		return this.weight;
	}
	
	public int rateWeighted(DrumPattern pattern, InputAnalysis analysis) {
		
		float rating = 0;
		int numOfFactors = 0;
		
		if (patternFactor != null) {
			rating += patternFactor.rate(pattern);
			numOfFactors++;
		}
		if (soloFactor != null) {
			rating += soloFactor.rate(analysis);
			numOfFactors++;
		}
		
		int fitness = (numOfFactors == 0) ? 0 : (int)((rating / numOfFactors) * LIMIT);
		
		setChanged();
		notifyObservers(fitness);
		
		return fitness;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(patternFactor=" + patternFactor + "," + "soloFactor=" + soloFactor + ")";
	}
}
