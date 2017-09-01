package genetic.mutations;

import genetic.DrumPattern;
import genetic.Random;
import init.Settings;
import init.Streams;

import java.util.ArrayList;

public class MutationManager {

	//TODO mutationManager überflüssig -> aufräumen
	
	private ArrayList<Mutation> list = new ArrayList<Mutation>();
	
	public MutationManager() {
		initiateMutations();
	}
	
	private void initiateMutations() {
		
		list.add(new RandomPointMutation());
//		list.add(new ReverseMutation());
//		list.add(new RotateMutation());
//		list.add(new SortMutation());
//		list.add(new LoudnessMutation());
//		list.add(new DegenerateMutation());
//		list.add(new ShuffleTicksMutation());
//		list.add(new ShuffleInstrumentsMutation());
		
		Streams.mutationOut.println("Mutations initiated.");
	}
	
	public ArrayList<Mutation> getList() {
		return this.list;
	}
	
	public void useRandomMutation(DrumPattern pattern) {
		
		int weightSum = 0;
		for (Mutation m : list) {
			weightSum += m.getWeight();
		}
		
		if (weightSum <= 0)
			return;
		
		final int randomWeight = Random.nextInt(weightSum);
		Mutation mutation = null;
		
		int weightCount = 0;
		for (Mutation m : list) {
			weightCount += m.getWeight();
			if (randomWeight < weightCount) {
				mutation = m;
				break;
			}
		}
		
		mutation.mutate(pattern);
	}
	
}
