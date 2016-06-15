package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class OriginalRule extends Rule {
	
	public OriginalRule() {
		super("KeepOriginal", "Rates up patterns close to the init one");
	}

	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//bewertet pattern hoch, die geringe distanz zum init pattern haben
		
		DrumPattern initPattern = Generation.getInitPatternCopy();

		// calculate the max distance
		float max = 0;
		for (int i = 0; i < initPattern.matrix.length; i++) {
			for (int j = 0; j < initPattern.matrix[i].length; j++) {
					max += Math.max(initPattern.matrix[i][j], 127 - initPattern.matrix[i][j]);
				}
			}
		
		// calculate the distance
		float sum = 0;
		for (int i = 0; i < pattern.matrix.length; i++) {
			for (int j = 0; j < pattern.matrix[i].length; j++) {
					sum += Math.abs(pattern.matrix[i][j] - initPattern.matrix[i][j]);
				}
			}
		
		float rate = LIMIT - (LIMIT * sum / max);
		
		rateWeighted(pattern, rate);
	}
}