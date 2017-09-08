package genetic;


public class Rateable implements Comparable<Rateable> {

	protected float fitness = 0;

	@Override
	public int compareTo(Rateable rateable) {
		return new Float(this.fitness).compareTo(rateable.fitness);
	}
	
}
