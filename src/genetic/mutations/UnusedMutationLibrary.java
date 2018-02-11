package genetic.mutations;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.Random;
import init.Streams;

import java.util.ArrayList;
import java.util.Collections;

public class UnusedMutationLibrary {
	
	private static float randomPoint = 0.775f;
	private static float reverse = 0.80f;
	private static float rotate = 0.825f;
	private static float sort = 0.85f;
	private static float loudness = 0.85f;
	private static float degenerate = 0.90f;
	private static float shuffleTicks = 0.95f;
	private static float shuffleInstruments = 1.0f;
	
	
	public static void random(DrumPattern pattern) {
		
		float random = Random.nextFloat();
		
		if (random < randomPoint) {
			UnusedMutationLibrary.randomPoint(pattern);
		} else if (random < reverse) {
			UnusedMutationLibrary.reverse(pattern);
		} else if (random < rotate) {
			UnusedMutationLibrary.rotate(pattern);
		} else if (random < sort) {
			UnusedMutationLibrary.sort(pattern);
		} else if (random < loudness) {
			UnusedMutationLibrary.loudness(pattern);
		} else if (random < degenerate) {
			UnusedMutationLibrary.degenerate(pattern);
		} else if (random < shuffleTicks) {
			UnusedMutationLibrary.shuffleTicks(pattern);
		} else if (random < shuffleInstruments) {
			UnusedMutationLibrary.shuffleInstruments(pattern);
		}
	}

	public static void randomPoint(DrumPattern p) {

		int i = Random.nextInt(p.matrix.length);
		int j = Random.nextInt(p.matrix[i].length);
		p.matrix[i][j] = Random.rangeInt(0, 127);
		
		// MESSAGE debug simple mutation message
		Streams.mutationOut.println("Point ("+i+","+j+")="+p.matrix[i][j]+" mutation..");
	}
	
	public static void reverse(DrumPattern p) {
		
		// MESSAGE debug reverse mutation message
		Streams.mutationOut.println("Reverse mutation..");
		
		int[][] newMatrix = new int[p.matrix.length][p.matrix[0].length];
		
		for (int i = 0; i < p.matrix.length; i++) {
			for (int j = 0; j < p.matrix[i].length; j++) {
				newMatrix[p.matrix.length-(i+1)][j] = p.matrix[i][j];
			}
		}
		
		p.matrix = newMatrix;
	}
	
	public static void rotate(DrumPattern p) {
		UnusedMutationLibrary.rotate(p, Random.rangeInt(1,p.matrix.length));
	}
	
	public static void rotate(DrumPattern p, int steps) {
		
		// MESSAGE debug rotate mutation message
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
	
	public static void sort(DrumPattern p) {
		UnusedMutationLibrary.sort(p, Random.nextBoolean());
	}
	
	public static void sort(DrumPattern p, boolean down) {
		
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
	
	public static void loudness(DrumPattern p) {
		UnusedMutationLibrary.loudness(p, Random.rangeInt(0, 127));
	}
	
	public static void loudness(DrumPattern p, float target) {
			
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
	
	public static void degenerate(DrumPattern p) {
		UnusedMutationLibrary.degenerate(p, Random.nextFloat());
	}
	
	public static void degenerate(DrumPattern p, float factor) {
		
		DrumPattern init = Generation.getInitPatternCopy();
		if (init == null) {
			return;
		}
		
		// MESSAGE debug degeneration mutation message
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
	
	public static void shuffleTicks(DrumPattern p) {
		
		// MESSAGE debug shuffleTicks mutation message
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
	
	public static void shuffleInstruments(DrumPattern p) {
		
		// MESSAGE debug shuffleInstruments mutation message
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
