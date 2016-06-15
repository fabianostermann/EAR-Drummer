package genetic;

import java.util.ArrayList;

public class Generation {

	private static DrumPattern initPattern = null;
	
	private static ArrayList<DrumPattern> population = null;
	private static ArrayList<DrumPattern> backupPopulation = null;
	
	private static boolean populationChanged = false;
	
	public synchronized static DrumPattern getNextPattern() {
		populationChanged = false;
		
		if (population != null && population.size() > 0) {
			int randomInt = (int)(Math.random()*(population.size()-1));
			DrumPattern nextPattern = population.get(randomInt);
			backupPopulation.add(population.remove(randomInt));
			return nextPattern;
		}
		else if (backupPopulation != null && backupPopulation.size() > 0) {
			return backupPopulation.get((int)(Math.random()*(backupPopulation.size()-1)));
		}
		else if (initPattern != null) {
			return initPattern;
		}
		else
			return null;
	}
	
	protected synchronized static void writePopulation(ArrayList<DrumPattern> population) {
		
		if (population == null) {
			Generation.population = null;
			Generation.backupPopulation = null;
		} else {
			
			Generation.population = new ArrayList<DrumPattern>();
			Generation.backupPopulation = new ArrayList<DrumPattern>();
			for (DrumPattern pattern : population) {
				Generation.population.add(pattern);
			}			
		}
		
		populationChanged = true;
	}
	
	public synchronized static void setInitPattern(DrumPattern initPattern) {
		Generation.initPattern = initPattern;
	}
	
	public synchronized static DrumPattern getInitPatternCopy() {
		if (initPattern != null)
			return initPattern.copy();
		else
			return null;
	}
	
	public static boolean populationChanged() {
		return populationChanged;
	}

	public synchronized static ArrayList<DrumPattern> getPopulationCopy() {
		
		if (Generation.population == null)
			return null;
		
		ArrayList<DrumPattern> populationCopy = new ArrayList<DrumPattern>();
		for (DrumPattern pattern : Generation.population) {
			populationCopy.add(pattern.copy());
		}
		for (DrumPattern pattern : Generation.backupPopulation) {
			populationCopy.add(pattern.copy());
		}
		return populationCopy;
	}
	
}
