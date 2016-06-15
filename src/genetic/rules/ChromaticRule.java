package genetic.rules;

import genetic.DrumPattern;
import input.InputAnalysis;

public class ChromaticRule extends Rule {
	
	public ChromaticRule() {
		super("Chromatic", null);
	}

	
	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//Belohnt hoch, wenn pattern lückenlos und solo sehr chromatisch
		
		if (analysis.numberOfNotes <= 1 ||
				analysis.intervalMin > 1) {
			return;
		}
		
		float chromaticFactor = 1 / Math.max(1f, analysis.intervalAverage);
		
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
				
		float average = (silenceFactor + chromaticFactor) / 2;
		
		float rate = average * LIMIT;
		
		rateWeighted(pattern, rate);
	}
	
}