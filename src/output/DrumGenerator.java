package output;

import genetic.RhythmNote;
import init.Settings;
import init.Streams;

import javax.sound.midi.ShortMessage;

public class DrumGenerator {

	//TODO make following chooseable via gui
	public static final int VOLUME_MIN = 32;
	public static final int CHANNEL = 9;
	
	private OutputGenerator outputGenerator;

	public DrumGenerator(OutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
	}
	
	public void play(RhythmNote note) {
		
		// MESSAGE debug playing note message
		if (Settings.DEBUG) 
			Streams.midiOut.println("Playing note: "+note);
		
		play(note.getMidi(), note.getVolume());
	}

	public void play(int midi, int volume) {
		
		if (volume >= VOLUME_MIN)
			this.outputGenerator.play(ShortMessage.NOTE_ON, CHANNEL, midi, volume);
		
	}

}
