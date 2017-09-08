package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import input.InputAnalysis;

public class OstinatoRule extends Rule {

	public OstinatoRule() {
		super("Ostinato", null);
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {

		//belohnt ostinato (repeating pattern) mit pattern, die zwei akzente pro schlag besitzen

		if (analysis.numberOfNotes <= 0 ||
				analysis.numberOfNotes * 3 < analysis.numberOfDifferentNotes) {
			return 0f;
		}
		
		int numberOfDifferentNotes = Math.max(1, analysis.numberOfDifferentNotes - 3);

		float ostinatoFactor = 1f - ((float)numberOfDifferentNotes / analysis.numberOfNotes);
		

		//erwünscht sind 2 mittellaute schläge pro tick
		int doubleAccentCount = 0;
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			int tickAccentCount = 0;
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > analysis.volumeAverage) { //akzent
					tickAccentCount += 1;
				}
			}
			if (tickAccentCount >= 2)
				doubleAccentCount += 1;
			
		}

		float accentFactor = Math.abs((float)doubleAccentCount - (float)pattern.getTicks()) + 1;
		accentFactor = 1f / (accentFactor * accentFactor);

		float average = (ostinatoFactor + accentFactor) / 2;
		
		return average;
	}

}