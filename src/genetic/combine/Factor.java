package genetic.combine;

import java.util.Observable;

public abstract class Factor extends Observable{
	
	public abstract String getName();
	
	@Override
	public String toString() {
		return getName();
	}
}
