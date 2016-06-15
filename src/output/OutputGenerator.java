package output;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class OutputGenerator implements Transmitter {

	private Receiver receiver;

	public OutputGenerator() {}

	public void play(int command, int channel, int midi, int volume) {
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
