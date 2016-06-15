package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import input.InputAnalysis;

public class PedalRule extends Rule {

	public PedalRule() {
		super("Pedal", null);
	}

	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {

		//belohnt lange noten mit pattern, die mindestens 2/3 pause haben

		if (analysis.numberOfNotes <= 0) {
			return;
		}
		
		
		float pedalFactor = Math.min(5000f, (float)analysis.lengthAverage) / 5000f;

		int silentTicks = 0;
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			boolean tickIsSilent = true;
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > 0) {
					tickIsSilent = false;
					break;
				}
			}
			
			if (tickIsSilent)
				silentTicks += 1;
		}
		
		float silenceFactor = silentTicks / (2* pattern.getTicks() / 3);
		silenceFactor = Math.min(silenceFactor, 1f);
		
		float average = (pedalFactor + silenceFactor) / 2;
		
		float rate = LIMIT * average;

		rateWeighted(pattern, rate);
	}

}