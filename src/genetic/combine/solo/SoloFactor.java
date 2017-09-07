package genetic.combine.solo;

import genetic.combine.Factor;
import input.InputAnalysis;

public abstract class SoloFactor extends Factor {

	public SoloFactor(String name) {
		super(name);
	}

	public abstract float rate(InputAnalysis analysis);	
}