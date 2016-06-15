package genetic.mutations;

import init.Settings;
import init.Streams;

import java.util.ArrayList;
import java.util.Collections;

import genetic.DrumPattern;

public class ShuffleTicksMutation extends Mutation {

	public ShuffleTicksMutation() {
		super("Shuffle Ticks Mutation", "Shuffles the patterns ticks");
	}
	
	@Override
	public void mutate(DrumPattern p) {
		
		// MESSAGE debug shuffleTicks mutation message
		if (Settings.DEBUG) 
			Streams.mutationOut.println("ShuffleTicks mutation..");
					
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
						
		for (int j = 0; j < p.matrix[0].length; j++) {
			ArrayList<Integer> volumes = new ArrayList<Integer>();
			for (int i = 0; i < p.matrix.length; i++) {
				volumes.add(p.matrix[i][j]);
			}
			Collections.shuffle(volumes);
			for (int i = 0; i < p.matrix.length; i++) {
				newMatrix[i][j] = volumes.get(i);
			}
		}
						
		p.matrix = newMatrix;
				
	}

}
