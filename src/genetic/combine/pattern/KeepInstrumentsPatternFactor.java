package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class KeepInstrumentsPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "keepInstruments";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		DrumPattern initPattern = Generation.getInitPatternCopy();

		int sum = 0;
		
		boolean initIsEmpty;
		boolean patternIsEmpty;
		
		for (int j = 0; j < pattern.matrix[0].length; j++) {
			
			initIsEmpty = true;
			patternIsEmpty = true;
			
			for (int i = 0; i < pattern.matrix.length; i++) {
				
					if (pattern.matrix[i][j] > 0)
						patternIsEmpty = false;
					
					if (initPattern.matrix[i][j] > 0)
						initIsEmpty = false;
			}
			
			if (initIsEmpty != patternIsEmpty)
				sum += 1;
			
		}
		
		float rate = 1f - ((float)sum / (pattern.getInstruments()));
		return rate;
	}

}
