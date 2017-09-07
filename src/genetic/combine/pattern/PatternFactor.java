package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.combine.Factor;

public abstract class PatternFactor extends Factor{
	
	public PatternFactor(String name) {
		super(name);
	}

	public abstract float rate(DrumPattern pattern);
}