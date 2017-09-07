package genetic.combine;

import java.util.Observable;

public abstract class Factor extends Observable{

	private String name = this.toString();

	public Factor(String name) {
		if (name != null) {
			this.name = name;			
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		if (name == null)
			return super.toString();
		else
			return getName();
	}
}
