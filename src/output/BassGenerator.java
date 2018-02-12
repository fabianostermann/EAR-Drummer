package output;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class BassGenerator {
	
	public static final int CHANNEL = 8;

	
	private OutputGenerator outputGenerator;

	public BassGenerator(OutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
		
		this.changeInstrument(GM1.Acoustic_Bass);
	}
	
	public void changeInstrument(int instrument) {
		try {
			outputGenerator.getReceiver().send(new ShortMessage(ShortMessage.PROGRAM_CHANGE, CHANNEL, instrument, 0), -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	
	private int lastMidi = 0;
	public void play(int midi, int volume) {
		
		this.outputGenerator.play(ShortMessage.NOTE_OFF, CHANNEL, lastMidi, 0);
		
		this.outputGenerator.play(ShortMessage.NOTE_ON, CHANNEL, midi, volume);
		lastMidi = midi;
	}

}
