package genetic.mutations;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import init.Settings;
import init.Streams;

public class DegenerateMutation extends Mutation {

	public DegenerateMutation() {
		super("Degenerate Mutation","Degenerates the pattern back to the init pattern");
	}
	
	@Override
	public void mutate(DrumPattern p) {
		
		//factor will be between 0 - 0.25
		float factor = Random.nextFloat() / 4;
			
		DrumPattern init = Generation.getInitPatternCopy();
		if (init == null) {
			return;
		}
		
		// MESSAGE debug degeneration mutation message
		if (Settings.DEBUG) 
			Streams.mutationOut.println("Degeneration "+factor+" mutation..");
		
		int[][] newMatrix = p.matrix.clone();
		
		for (int i = 0; i < p.matrix.length; i++) {
			for (int j = 0; j < p.matrix[i].length; j++) {
				if (Random.nextBoolean(factor))
					newMatrix[i][j] = init.matrix[i][j];
			}
		}
				
		p.matrix = newMatrix;
	}

}
