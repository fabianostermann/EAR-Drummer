package genetic;


public class Rateable implements Comparable<Rateable> {

	public int fitness = 0;

	@Override
	public int compareTo(Rateable rateable) {
		return (int) (this.fitness - rateable.fitness);
	}
	
}
