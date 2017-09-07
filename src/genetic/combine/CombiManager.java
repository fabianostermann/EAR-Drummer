package genetic.combine;

import genetic.combine.pattern.PatternFactor;
import genetic.combine.pattern.RandomPatternFactor;
import genetic.combine.solo.RandomSoloFactor;
import genetic.combine.solo.SoloFactor;
import init.Streams;

import java.util.ArrayList;

public class CombiManager {

	private ArrayList<Combi> combis = new ArrayList<Combi>();
	
	private PatternFactor[] patternFactors = {
			new RandomPatternFactor()
	};
	private SoloFactor[] soloFactors = {
			new RandomSoloFactor()
	};
	
	public CombiManager() {
		initDebugCombinations();
	}

	private void initDebugCombinations() {
		
		Combi random = new Combi();
		random.patternFactor = patternFactors[0];
		random.soloFactor = soloFactors[0];
		combis.add(random);
		
		Streams.CombiOut.println("Initiated combis with " + combis);
	}
	
	public ArrayList<Combi> getList() {
		return this.combis;
	}
	
}
