package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class InstrumentJumpsPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "instrumentJumps";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {

		int instrumentJumps = 0;
		int lastInstrumentMax = -1;
		int instrumentMax = -1;
		
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > (analysis.volumeAverage)) {
					instrumentMax = Math.max(instrumentMax, pattern.matrix[i][j]);
				}
			}
			
			if (instrumentMax != -1 && instrumentMax != lastInstrumentMax) {
				instrumentJumps++;
			}
			lastInstrumentMax = instrumentMax;
			instrumentMax = -1;
			
		}
		
				
		float average = (float)instrumentJumps / pattern.getTicks();
		return average;
	}

}
