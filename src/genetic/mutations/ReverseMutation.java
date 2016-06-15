package genetic.mutations;

import init.Settings;
import init.Streams;
import genetic.DrumPattern;

public class ReverseMutation extends Mutation {

	public ReverseMutation() {
		super("Reverse Mutation", "Reverses the patterns matrix");
	}
	
	@Override
	public void mutate(DrumPattern p) {

		// MESSAGE debug reverse mutation message
		if (Settings.DEBUG) 
			Streams.mutationOut.println("Reverse mutation..");
		
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
		
		for (int i = 0; i < p.matrix.length; i++) {
			for (int j = 0; j < p.matrix[i].length; j++) {
				newMatrix[p.matrix.length-(i+1)][j] = p.matrix[i][j];
			}
		}
		
		p.matrix = newMatrix;
	}

}
