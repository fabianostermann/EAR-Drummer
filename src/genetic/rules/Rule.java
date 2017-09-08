package genetic.rules;

import genetic.DrumPattern;
import init.Settings;
import init.Streams;
import input.InputAnalysis;

import java.util.Observable;

public abstract class Rule extends Observable {
	
	protected String name = this.toString();
	protected String description = "No description available";
	
	public Rule(String name, String description) {
		
		if (name != null) {
			this.name = name;			
		}
		
		if (description != null && description.length() > 0) {
			this.description = description;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	protected float weight = 1f;
	
	public void setWeight(float weight) {
		weight = Math.max(0f, weight);
		weight = Math.min(1f, weight);
		
		this.weight = weight;
	}
	
	public float getWeight() {
		return this.weight;
	}

	
	public abstract float rate(DrumPattern pattern, InputAnalysis analysis);
	
	protected float getWeightedRating(DrumPattern pattern, InputAnalysis analysis) {
		
		float rating = rate(pattern, analysis);
		
		if (rating > 1f || rating < 0f) {
			
			System.err.println("Rule.rateWeighted(): Rate value of rule "+this.name+" not in bounds: "+rating);
			
			rating = Math.min(1f, rating);
			rating = Math.max(0f, rating);
		}
		
		float weightedRate = this.weight * rating;
		Streams.ruleOut.println("Rule "+this.name+" rated pattern fitness "+weightedRate);
		
		setChanged();
		notifyObservers(weightedRate);

		return weightedRate;
	}

}