package genetic;

import init.Settings;

public class Recombination {
	
	public static void crossover(DrumPattern p1, DrumPattern p2) {
		
		if (!(p1.matrix.length < 2))
			crossover(p1,p2,Random.rangeInt(1, p1.matrix.length-1));
	}
	
	public static void crossover(DrumPattern p1, DrumPattern p2, int point) {
		
		//MESSAGE debug crossover message
		System.out.println("Crossover at point("+point+")..");
		
		if (point <= 0 || point >= p1.matrix.length)
			return;
		
		for (int i = point; i < p1.matrix.length; i++) {
			for (int j = 0; j < p1.matrix[i].length; j++) {
				int tmp = p1.matrix[i][j];
				p1.matrix[i][j] = p2.matrix[i][j];
				p2.matrix[i][j] = tmp;
			}
		}
	}
	
	public static void add(DrumPattern p1, DrumPattern p2) {
		
	}
	
	public static void substract(DrumPattern p1, DrumPattern p2) {
		
	}

}
