package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class MorePausePatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "morePause";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		int silentTicks = 0;
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			boolean tickIsSilent = true;
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > 0) {
					tickIsSilent = false;
					break;
				}
			}
			
			if (tickIsSilent)
				silentTicks += 1;
		}
		
		float silenceFactor = silentTicks / (2* pattern.getTicks() / 3);
		silenceFactor = Math.min(silenceFactor, 1f);
		return silenceFactor;
	}

}
