package genetic.rules;

import genetic.DrumPattern;
import input.InputAnalysis;

public class StaccatoRule extends Rule {
	
	public StaccatoRule() {
		super("Staccato", null);
	}

	@Override
	public float rate(DrumPattern pattern, InputAnalysis analysis) {
		
		//belohnt staccato spielen mit akzenten
		
		if (analysis.numberOfNotes <= 0 ||
				2*analysis.lengthAverage > analysis.distanceAverage  ||
				analysis.lengthAverage > 380d) {
			return 0f;
		}
		
		float lengthAverage =  Math.max(80f, (float)analysis.lengthAverage) - 80f;
		float staccatoFactor = 1f - (lengthAverage / 380f);
		
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
		
		float average = (accentFactor + staccatoFactor) / 2;
		
		return average;
	}
	
}