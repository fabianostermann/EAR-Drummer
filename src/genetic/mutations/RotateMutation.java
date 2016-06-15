package genetic.mutations;

import init.Settings;
import init.Streams;
import genetic.DrumPattern;
import genetic.Random;

public class RotateMutation extends Mutation {
	
	public RotateMutation() {
		super("Rotate Mutation","Rotates the matrix a random step further");
	}

	@Override
	public void mutate(DrumPattern p) {
		
		int steps =  Random.rangeInt(1,p.matrix.length);
			
		// MESSAGE debug rotate mutation message
		if (Settings.DEBUG) 
			Streams.mutationOut.println("Rotate "+steps+" mutation..");
		
		if (steps < 0) {
			steps = -steps;
			steps = steps % p.matrix.length;
			steps = p.matrix.length-steps;
		}
		
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
		
		for (int i = 0; i < p.matrix.length; i++) {
			for (int j = 0; j < p.matrix[i].length; j++) {
				newMatrix[(i+steps)%p.matrix.length][j] = p.matrix[i][j];
			}
		}
			
		p.matrix = newMatrix;
	}


}
