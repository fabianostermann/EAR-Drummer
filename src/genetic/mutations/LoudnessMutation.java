package genetic.mutations;

import genetic.DrumPattern;
import genetic.Random;
import init.Streams;

public class LoudnessMutation extends Mutation {

	public LoudnessMutation() {
		super("Loudness Mutation","Approximates the matrix average loudness to a random target loudness");
	}
	
	@Override
	public void mutate(DrumPattern p) {

		float target = Random.rangeInt(0, 127);
		
		target = Math.max(0, target);
		target = Math.min(127, target);
		
		// MESSAGE debug loudness mutation message
		Streams.mutationOut.println("Loudness "+target+" mutation..");
		
		int[][] newMatrix = p.matrix.clone();
			
		while (true) {
			float avgVolume = 0;
			int numberOfNotes = 0;
			for (int i = 0; i < newMatrix.length; i++) {
					for (int j = 0; j < newMatrix[i].length; j++) {
					if (newMatrix[i][j] > 0) {
						avgVolume += newMatrix[i][j];
						numberOfNotes++;
					}
				}
			}
			if (numberOfNotes <= 0) {
				break;
			}
			
			avgVolume = avgVolume / numberOfNotes;
		
			float diff = target - avgVolume;
			
			if (Math.abs(diff) < 1)
				break;
			
			for (int i = 0; i < newMatrix.length; i++) {
				for (int j = 0; j < newMatrix[i].length; j++) {
					if (newMatrix[i][j] > 0) {
						newMatrix[i][j] += diff;
						newMatrix[i][j] = Math.max(0, newMatrix[i][j]);
						newMatrix[i][j] = Math.min(127, newMatrix[i][j]);
					}
				}
			}
		}
					
		p.matrix = newMatrix;
	}

}
