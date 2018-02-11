package playback;

import genetic.DrumPattern;
import genetic.Generation;
import genetic.RhythmNote;
import init.Streams;

import java.util.Observable;

import output.DrumGenerator;

public class PatternPlayer extends Observable implements MetronomeListener {

	private DrumGenerator drumGenerator;
	private DrumPattern pattern;
	
	public PatternPlayer(DrumGenerator drumGenerator) {
		this.setDrumGenerator(drumGenerator);
	}
	
	@Override
	public void tick(Metronome metronome) {
		
		if (metronome.getTick() == 0 || Generation.populationChanged()) {
			this.setPattern(Generation.getNextPattern());
			
			// MESSAGE debug patternPlayer nextPattern message
			Streams.midiOut.println("Next Pattern Request at tick("+metronome.getTick()+")");
		}
		
		if (pattern != null && drumGenerator != null) {
			for (int instrument = 0; instrument < pattern.getInstruments(); instrument++) {
				
				int volume = pattern.get(metronome.getTick(), instrument);

				if (volume > 0) {
					RhythmNote note = new RhythmNote(instrument, volume);
					drumGenerator.play(note);
				}
			}
			
		}

	}
	
	public void setPattern(DrumPattern pattern) {
		this.pattern = pattern;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public DrumPattern getPattern() {
		return this.pattern;
	}

	public DrumGenerator getDrumGenerator() {
		return this.drumGenerator;
	}

	private void setDrumGenerator(DrumGenerator drumGenerator) {
		this.drumGenerator = drumGenerator;
	}

}
