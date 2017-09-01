package playback;

import genetic.Evolution;
import genetic.Random;
import output.BassGenerator;

public class PrimitiveBassist implements MetronomeListener {

	//TODO think about bass
	
	public static final int C2 = 36;
	public static final int Db2 = 37;
	public static final int D2 = 38;
	public static final int Eb2 = 39;
	public static final int E2 = 40;
	public static final int F2 = 41;
	public static final int Gb2 = 42;
	public static final int G2 = 43;
	public static final int Ab2 = 44;
	public static final int A2 = 45;
	public static final int Bb2 = 46;
	public static final int B2 = 47;
	public static final int C3 = 48;
	public static final int Db3 = 49;
	public static final int D3 = 50;
	public static final int Eb3 = 51;
	public static final int E3 = 52;
	public static final int F3 = 53;
	public static final int Gb3 = 54;
	public static final int G3 = 55;
	public static final int Ab3 = 56;
	public static final int A3 = 57;
	public static final int Bb3 = 58;
	public static final int B3 = 59;
	public static final int C4 = 60;
	
	public static final int[] Cm7 = new int[] { C2, Eb2, G2, Bb2};
	public static final int[] Ebm7 = new int[] { Eb2, Gb2, Bb2, Db3};
	public static final int[] Fm7 = new int[] { F2, Ab2, C3, Eb3 };
	public static final int[] Db7 = new int[] { Db2, F2, Ab2, B2 };
	public static final int[] Dm7 = new int[] { D2, F2, A2, C3 };
	public static final int[] Dm7b5 = new int[] { D2, F2, Ab2, C3 };
	public static final int[] G7 = new int[] { G2, B2, D2, F3 };
	public static final int[] Ab7 = new int[] { Ab2, C2, Eb2, Gb3 };
	public static final int[] Dbmaj7 = new int[] { Db2, F2, Ab2, B3 };
	
	public static int[][] chordChart = { Cm7, Cm7, Fm7, Fm7,
										Dm7b5, G7, Cm7, Cm7,
										Ebm7, Ab7, Dbmaj7, Dbmaj7,
										Dm7b5, G7, Cm7, G7 };
//										Ebm7, Ebm7, Ebm7, Ebm7,
//										Cm7, Cm7, Dm7, Db7 };
	private int[] currentChord = chordChart[0];
	private int count = 0;
	
	public int volume = 90;
	
	boolean swing = false;
	boolean bossa = true;
	
	private BassGenerator bassGenerator;
	private Evolution evolution;

	public PrimitiveBassist(BassGenerator bassGenerator, Evolution evolution) {
		
		this.bassGenerator = bassGenerator;
		this.evolution = evolution;
	}
	
	public void reset() {
		count = 0;
	}
	
	private boolean enabled = true;
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public void tick(Metronome metronome) {
		if (!enabled) {
			return;
		}
		
		if (swing) {
	//		int volumeAvg = 0;
	//		if (evolution.getInputAnalysis() != null) {
	//			volumeAvg = (int) evolution.getInputAnalysis().volumeAverage;
	//		}
	//		if (volumeAvg > 0)
	//			volume = volumeAvg;
	//		else
	//			volume = 100;
			
			if (metronome.getTick() == 0) {
				currentChord = chordChart[count % chordChart.length];
				count++;
			}
			
			if (metronome.getTick() == 0) {
				bassGenerator.play(currentChord[0], volume);
				return;
			}
			
			if (metronome.getTick() % 2 == 0) {
				if (metronome.getTick() >= metronome.getTicks()-2) {
					int chromatic;
					if (Random.nextBoolean())
						chromatic = currentChord[0]-1;
					else
						chromatic = currentChord[0]+1;
					
					bassGenerator.play(chromatic, volume);
					return;
				}
				
				int octaveDown = 0;
				if (Random.nextBoolean())
					octaveDown = 12;
				
				bassGenerator.play(currentChord[Random.rangeInt(1, 4)] - octaveDown, volume);
				return;
			}
		} else if (bossa) {
			
			if (metronome.getTick() == metronome.getTicks()-1) {
				count++;
				currentChord = chordChart[count % chordChart.length];
			}
			
			if (metronome.getTick() == 0) {
				bassGenerator.play(currentChord[0], volume);
				return;
			}
			if (metronome.getTick() == 3 || metronome.getTick() == 4) {
				bassGenerator.play(currentChord[2], volume);
				return;
			}
			if (metronome.getTick() == metronome.getTicks()-1) {
				bassGenerator.play(currentChord[0], volume);
				return;
			}
		}
	}

}
