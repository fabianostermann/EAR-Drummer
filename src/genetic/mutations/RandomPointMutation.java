package genetic.mutations;

import init.Settings;
import init.Streams;
import genetic.DrumPattern;
import genetic.Random;

public class RandomPointMutation extends Mutation {

	public static int VOLUME_MIN = 32;
	
	public RandomPointMutation() {
		super("Random Point Mutation", "Mutates any point in the matrix");
	}

	@Override
	public void mutate(DrumPattern p) {
		
		int i,j;
		
		while(true) {
			i = Random.nextInt(p.matrix.length);
			j = Random.nextInt(p.matrix[i].length);
			
			int random = Random.rangeInt(0, 127);
			if (random < VOLUME_MIN)
				random = 0;
			
			if (p.matrix[i][j] != random) {
				p.matrix[i][j] = random;
				break;
			}
		}
		
		// MESSAGE debug simple mutation message
		Streams.mutationOut.println("Point ("+i+","+j+")="+p.matrix[i][j]+" mutation..");
	}

}
