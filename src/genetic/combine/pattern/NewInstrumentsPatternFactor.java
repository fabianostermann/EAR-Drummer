package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class NewInstrumentsPatternFactor extends PatternFactor {

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		DrumPattern initPattern = Generation.getInitPatternCopy();
				
		int sum = 0;
		int count = 0;
		boolean initIsEmpty;
		boolean patternIsEmpty;
		
		for (int j = 0; j < pattern.matrix[0].length; j++) {
			
			initIsEmpty = true;
			patternIsEmpty = true;
			
			for (int i = 0; i < pattern.matrix.length; i++) {
				
					if (pattern.matrix[i][j] > (analysis.volumeAverage))
						patternIsEmpty = false;
					
					if (initPattern.matrix[i][j] > 0)
						initIsEmpty = false;
			}
			
			if (initIsEmpty) {
				count += 1;
				if (!patternIsEmpty)
					sum += 1;
			}
			
		}
		float drumFactor;

		if (count <= 0) {
			drumFactor = 1f;
		} else {
			drumFactor = (float)sum / count;
		}

		return drumFactor;
	}

	@Override
	public String getName() {
		return "newInstruments";
	}

}
