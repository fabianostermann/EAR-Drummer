package genetic.combine.pattern;

import genetic.DrumPattern;
import input.InputAnalysis;

public class MoreTicksPatternFactor extends PatternFactor {

	@Override
	public String getName() {
		return "moreTicks";
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		
		int unsilentTicks = pattern.getTicks();
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			boolean tickIsSilent = true;
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > (analysis.volumeAverage / 2)) {
					tickIsSilent = false;
					break;
				}
			}
			
			if (tickIsSilent)
				unsilentTicks -= 1;
		}
		float silenceFactor = ((float)unsilentTicks / pattern.getTicks());
		return silenceFactor;
	}

}
