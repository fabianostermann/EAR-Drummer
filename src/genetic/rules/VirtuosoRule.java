package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class VirtuosoRule extends Rule {

	public VirtuosoRule() {
		super("Virtuoso", null);
	}

	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {

		//belohnt viele noten mit pattern die auch andere instrumente als init pattern haben

		if (analysis.numberOfNotes <= 0) {
			return;
		}

		float virtuosoFactor = 1f - (1f / analysis.numberOfNotes);
		virtuosoFactor = virtuosoFactor * virtuosoFactor * virtuosoFactor * virtuosoFactor;
		
		//drumFactor berechnen
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


		float average = (virtuosoFactor + drumFactor) / 2;

		float rate = LIMIT * average;

		rateWeighted(pattern, rate);
	}

}