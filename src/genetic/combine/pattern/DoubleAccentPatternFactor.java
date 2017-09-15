package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class DoubleAccentPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "doubleAccent";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
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

		return accentFactor;
	}

}
