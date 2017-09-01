package genetic.rules;

import genetic.DrumPattern;
import init.Settings;
import init.Streams;
import input.InputAnalysis;

import java.util.Observable;

public abstract class Rule extends Observable {

	//TODO normalize all rules to (0,1000)
	
	public static final int LIMIT = 1000;
	
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
		weight = Math.max(-1f, weight);
		weight = Math.min(1f, weight);
		
		this.weight = weight;
	}
	
	public float getWeight() {
		return this.weight;
	}

	
	public abstract void rate(DrumPattern pattern, InputAnalysis analysis);
	
	protected void rateWeighted(DrumPattern pattern, float rate) {
		
		float weightedRate = this.weight * rate;
		
		//TODO range check for weights < 0
		if (this.weight > 0 && (weightedRate > LIMIT || weightedRate < 0)) {
			
			System.err.println("Rule.rateWeighted(): Rate value of rule "+this.name+" not in bounds: "+weightedRate);
			
			weightedRate = Math.min(LIMIT, weightedRate);
			weightedRate = Math.max(0, weightedRate);
		}
		
		Streams.ruleOut.println("Rule "+this.name+" rated pattern fitness "+(int)weightedRate);
		
		pattern.fitness += (int)(weightedRate);
		
		setChanged();
		notifyObservers(weightedRate);
		
	}

}