package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import input.InputAnalysis;

public class FreeJazzRule extends Rule {

	public FreeJazzRule() {
		super("FreeJazz", null);
	}

	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {

		//belohnt das benutzen von mehr als 7 noten mit zufälligem pattern

		if (analysis.numberOfDifferentNotesOctave <= 7) {
			return;
		}

		float freeJazzFactor = 1f - (1f / analysis.numberOfDifferentNotesOctave) + 0.08f;
		freeJazzFactor = freeJazzFactor * freeJazzFactor;
		
		float random = (float) Random.nextFloat();

		float average = (freeJazzFactor + random) / 2;

		float rate = LIMIT * average;

		rateWeighted(pattern, rate);
	}

}