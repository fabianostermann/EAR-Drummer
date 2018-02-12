package output;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class OutputGenerator implements Transmitter {

	private Receiver receiver;

	public OutputGenerator() {}

	public void play(int command, int channel, int midi, int volume) {
		
		if (midi > 127 || midi < 0)
			System.err.println("Received Midi Note "+midi+" out of midi range [0,127].");
		while (midi > 127)// correct range top
			midi -= 12;
		while (midi < 0)// correct range bottom
			midi += 12;
		
		try {
			if (receiver != null) {
				receiver.send(new ShortMessage(command, channel, midi, volume), -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public void close() {
		this.receiver = null;
	}

}
