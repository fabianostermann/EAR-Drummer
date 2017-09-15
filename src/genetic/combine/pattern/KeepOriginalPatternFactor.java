package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class KeepOriginalPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "keepOriginal";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
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
		
		float rate = 1f - (sum / max);
		return rate;
	}

}
