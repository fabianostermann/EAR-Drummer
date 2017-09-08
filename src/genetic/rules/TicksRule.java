package genetic.rules;

import genetic.DrumPattern;
import genetic.Generation;
import input.InputAnalysis;

public class TicksRule extends Rule {
	
	public TicksRule() {
		super("KeepTicks", null);
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//bewertet pattern hoch, die auf den gleichen ticks auch schläge bzw. auch pausen haben
		
		DrumPattern initPattern = Generation.getInitPatternCopy();

		int sum = 0;
		
		boolean initIsEmpty;
		boolean tickIsEmpty;
		
		for (int i = 0; i < pattern.matrix.length; i++) {
			
			initIsEmpty = true;
			tickIsEmpty = true;
			
			for (int j = 0; j < pattern.matrix[i].length; j++) {
				
					if (pattern.matrix[i][j] > 0)
						tickIsEmpty = false;
					
					if (initPattern.matrix[i][j] > 0)
						initIsEmpty = false;
			}
			
			if (initIsEmpty != tickIsEmpty)
				sum += 1;
			
		}
		
		float rate = 1f - (sum / (pattern.getTicks()));
		
		return rate;
	}
}