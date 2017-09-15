package genetic.combine.solo;

import input.InputAnalysis;

public class StaccatoSoloFactor extends SoloFactor {

	@Override
	public String getName() {
		return "staccato";
	}

	@Override
	public float rate(InputAnalysis analysis) {
		if (analysis.numberOfNotes <= 0 ||
				2*analysis.lengthAverage > analysis.distanceAverage  ||
				analysis.lengthAverage > 380d) {
			return 0f;
		}
		
		float lengthAverage =  Math.max(80f, (float)analysis.lengthAverage) - 80f;
		float staccatoFactor = 1f - (lengthAverage / 380f);
		return staccatoFactor;
	}

}
