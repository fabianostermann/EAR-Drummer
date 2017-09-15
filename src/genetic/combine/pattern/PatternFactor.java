package genetic.combine.pattern;

import genetic.DrumPattern;
import genetic.combine.Factor;
import input.InputAnalysis;

public abstract class PatternFactor extends Factor{
	
	public abstract float rate(DrumPattern pattern, InputAnalysis analysis);
}