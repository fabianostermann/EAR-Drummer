package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import input.InputAnalysis;

public class WideRule extends Rule {

	public WideRule() {
		super("Wide", null);
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {

		//belohnt weite linien mit pattern, die alle instrumente einmal benutzen

		if (analysis.numberOfNotes <= 0) {
			return 0f;
		}
		
		float rangeFactor = (float) Math.min(analysis.keyRange, 36) / 36;
		
		
		int instrumentsCount = 0;
		for (int j = 0; j < pattern.matrix[0].length; j++) {
			for (int i = 0; i < pattern.matrix.length; i++) {

				if (pattern.matrix[i][j] > analysis.volumeAverage) {
					instrumentsCount += 1;
					break;
				}
			}
		}
		
		float drumFactor = (float) instrumentsCount / pattern.getInstruments();
		
		float average = (rangeFactor + drumFactor) / 2;
		
		return average;
	}

}