package genetic.mutations;

import genetic.DrumPattern;

public abstract class Mutation {

	protected String name = this.toString();
	protected String description = "No description available";
	
	public Mutation(String name, String description) {
		
		if (name != null) {
			this.name = name;			
		}
		
		if (description != null) {
			this.description = description;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	protected int weight = 100;
	
	public void setWeight(int weight) {
		weight = Math.max(0, weight);
		weight = Math.min(100, weight);
		
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}

	
	public abstract void mutate(DrumPattern p);

}