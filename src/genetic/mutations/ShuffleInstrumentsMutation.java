package genetic.mutations;

import init.Settings;
import init.Streams;

import java.util.ArrayList;
import java.util.Collections;

import genetic.DrumPattern;

public class ShuffleInstrumentsMutation extends Mutation {

	public ShuffleInstrumentsMutation() {
		super("Shuffle Instruments Mutation","Shuffles the patterns instruments");
	}
	
	@Override
	public void mutate(DrumPattern p) {
		
		// MESSAGE debug shuffleInstruments mutation message
		if (Settings.DEBUG) 
			Streams.mutationOut.println("shuffleInstruments mutation..");
		
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		
			for (int j = 0; j < p.matrix[0].length; j++) {
		ArrayList<Integer> volumes = new ArrayList<Integer>();
			for (int i = 0; i < p.matrix.length; i++) {
					volumes.add(p.matrix[i][j]);
				
				}
			list.add(volumes);
			}
			
			Collections.shuffle(list);
			
			for (int j = 0; j < p.matrix[0].length; j++) {
		ArrayList<Integer> volumes = list.get(j);
			for (int i = 0; i < p.matrix.length; i++) {
					newMatrix[i][j] = volumes.get(i);
				}
		}
			
		p.matrix = newMatrix;
	}

}
