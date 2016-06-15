package genetic.rules;

import genetic.DrumPattern;
import input.InputAnalysis;

public class LegatoRule extends Rule {
	
	public LegatoRule() {
		super("Legato", null);
	}

	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//belohnt legato spielen mit lückenlosen pattern
		
		if (analysis.numberOfNotes <= 1) {
			return;
		}
		
		float gapAverage = (float) Math.max(1f, analysis.gapAverage);
		float legatoFactor = (float) (1d / Math.sqrt(gapAverage));
		
		int unsilentTicks = pattern.getTicks();
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			boolean tickIsSilent = true;
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > (analysis.volumeAverage / 2)) {
					tickIsSilent = false;
				}
			}
			
			if (tickIsSilent)
				unsilentTicks -= 1;
		}
		
		float average = (((float)unsilentTicks / pattern.getTicks()) + legatoFactor) / 2;
		
		float rate = LIMIT * average;
		
		rateWeighted(pattern, rate);
	}
	
}