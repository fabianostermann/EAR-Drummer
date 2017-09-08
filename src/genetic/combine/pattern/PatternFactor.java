package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.combine.Factor;

public abstract class PatternFactor extends Factor{
	
	public abstract float rate(DrumPattern pattern);
}