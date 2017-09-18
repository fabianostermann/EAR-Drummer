package genetic.combine;

import genetic.DrumPattern;
import genetic.FitnessEvaluator;
import genetic.combine.pattern.AccentPatternFactor;
import genetic.combine.pattern.AllInstrumentsPatternFactor;
import genetic.combine.pattern.DoubleAccentPatternFactor;
import genetic.combine.pattern.EmptyPatternFactor;
import genetic.combine.pattern.InstrumentJumpsPatternFactor;
import genetic.combine.pattern.KeepInstrumentsPatternFactor;
import genetic.combine.pattern.KeepOriginalPatternFactor;
import genetic.combine.pattern.KeepTicksPatternFactor;
import genetic.combine.pattern.MorePausePatternFactor;
import genetic.combine.pattern.MoreTicksPatternFactor;
import genetic.combine.pattern.NewInstrumentsPatternFactor;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.pattern.RandomPatternFactor;
import genetic.combine.solo.ChromaticSoloFactor;
import genetic.combine.solo.EmptySoloFactor;
import genetic.combine.solo.FreeJazzSoloFactor;
import genetic.combine.solo.HoldsworthSoloFactor;
import genetic.combine.solo.LegatoSoloFactor;
import genetic.combine.solo.OstinatoSoloFactor;
import genetic.combine.solo.PedalSoloFactor;
import genetic.combine.solo.RandomSoloFactor;
import genetic.combine.solo.SoloFactor;
import genetic.combine.solo.StaccatoSoloFactor;
import genetic.combine.solo.VirtuosoSoloFactor;
import genetic.combine.solo.WideSoloFactor;
import init.Streams;
import input.InputAnalysis;

import java.util.ArrayList;

public class CombiManager implements FitnessEvaluator {

	private ArrayList<Combi> combis = new ArrayList<Combi>();
	
	// TODO introduce loudness as special factor
	
	private final PatternFactor[] patternFactors = {
			new EmptyPatternFactor(),
			new AccentPatternFactor(),
			new AllInstrumentsPatternFactor(),
			new DoubleAccentPatternFactor(),
			new InstrumentJumpsPatternFactor(),
			new KeepInstrumentsPatternFactor(),
			new KeepOriginalPatternFactor(),
			new KeepTicksPatternFactor(),
			new MorePausePatternFactor(),
			new MoreTicksPatternFactor(),
			new NewInstrumentsPatternFactor(),
			new RandomPatternFactor()
	};
	private final SoloFactor[] soloFactors = {
			new EmptySoloFactor(),
			new ChromaticSoloFactor(),
			new FreeJazzSoloFactor(),
			new HoldsworthSoloFactor(),
			new LegatoSoloFactor(),
			new OstinatoSoloFactor(),
			new PedalSoloFactor(),
			new StaccatoSoloFactor(),
			new VirtuosoSoloFactor(),
			new WideSoloFactor(),
			new RandomSoloFactor()
	};
	
	public CombiManager() {
		initCombinations();
	}

	private void initCombinations() {
		
		combis.add(new Combi());
		
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
		
		Streams.combiOut.println("Overall fitness of "+pattern+"="+fitness);
		return fitness;
	}

	public void addEmptyCombi() {
		this.addCombi(new Combi());
		
		Streams.combiOut.println("Added new combi: " + combis);
	}

	public PatternFactor getPatternFactor(String name) {
		for (PatternFactor patternFactor : patternFactors)
			if (patternFactor.getName().equals(name))
				return patternFactor;
		return new EmptyPatternFactor();
	}
	
	public SoloFactor getSoloFactor(String name) {
		for (SoloFactor soloFactor : soloFactors)
			if (soloFactor.getName().equals(name))
				return soloFactor;
		return new EmptySoloFactor();
	}
	
	public void addCombi(Combi combi) {
		combis.add(combi);
	}

	public void removeCombi(Combi combi) {
		combis.remove(combi);
		
		Streams.combiOut.println("Removed a combi: " + combis);
	}
}
