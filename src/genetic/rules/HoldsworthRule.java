package genetic.rules;

import genetic.DrumPattern;
import input.InputAnalysis;

public class HoldsworthRule extends Rule {
	
	public HoldsworthRule() {
		super("Holdsworth", null);
	}

	
	@Override
	public void rate(DrumPattern pattern, InputAnalysis analysis) {

		//Belohnt hoch, wenn pattern instrumentensprünge und solo große intervalle
		
		if (analysis.numberOfNotes <= 1 ||
				analysis.intervalAverage < 5) {
			return;
		}
		
		float averageDistance = analysis.intervalAverage / 12;
		averageDistance = Math.min(1f, averageDistance);
		
		int instrumentJumps = 0;
		int lastInstrumentMax = -1;
		int instrumentMax = -1;
		
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				if (pattern.matrix[i][j] > (analysis.volumeAverage)) {
					instrumentMax = Math.max(instrumentMax, pattern.matrix[i][j]);
				}
			}
			
			if (instrumentMax != -1 && instrumentMax != lastInstrumentMax) {
				instrumentJumps++;
			}
			lastInstrumentMax = instrumentMax;
			instrumentMax = -1;
			
		}
		
				
		float average = (((float)instrumentJumps / pattern.getTicks()) + averageDistance) / 2;
		
		float rate = average * LIMIT;
		
		rateWeighted(pattern, rate);
	}
	
}