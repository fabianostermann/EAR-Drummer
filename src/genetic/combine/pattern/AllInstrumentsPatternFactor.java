package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class AllInstrumentsPatternFactor extends PatternFactor {

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {

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
		return drumFactor;
	}

	@Override
	public String getName() {
		return "allInstruments";
	}

}
