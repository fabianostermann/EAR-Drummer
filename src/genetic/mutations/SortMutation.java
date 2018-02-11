package genetic.mutations;

import genetic.DrumPattern;
import genetic.Random;
import init.Streams;

import java.util.ArrayList;
import java.util.Collections;

public class SortMutation extends Mutation {

	public SortMutation() {
		super("Sort Mutation","Sort the matrix randomly up or down");
	}
	
	@Override
	public void mutate(DrumPattern p) {

		boolean down = Random.nextBoolean();
			
		// MESSAGE debug sort mutation message
		if (down)
			Streams.mutationOut.println("Sort down mutation..");
		else
			Streams.mutationOut.println("Sort up mutation..");
		
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
		
		for (int j = 0; j < p.matrix[0].length; j++) {
			ArrayList<Integer> volumes = new ArrayList<Integer>();
			for (int i = 0; i < p.matrix.length; i++) {
				volumes.add(p.matrix[i][j]);
			}
				Collections.sort(volumes);
			if (down) {
				Collections.reverse(volumes);
				}
			for (int i = 0; i < p.matrix.length; i++) {
					newMatrix[i][j] = volumes.get(i);
			}
		}
		
		p.matrix = newMatrix;
		
	}
}
