package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class AccentPatternFactor extends PatternFactor {

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		//erwünscht ist 1 Akzent pro 3 ticks
		int accentCount = 0;
		for (int i = 0; i < pattern.matrix.length; i++) {
			for (int j = 0; j < pattern.matrix[i].length; j++) {
					if (pattern.matrix[i][j] >= 110) { //akzent
						accentCount++;
						break;
					}
				}
			}
		
		float accentFactor = Math.abs((float)accentCount - ((float)pattern.getTicks() / 3)) + 1;
		accentFactor = 1f / accentFactor;
		return accentFactor;
	}

	@Override
	public String getName() {
		return "accent";
	}

}
