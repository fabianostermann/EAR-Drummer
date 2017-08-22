package input;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import record.SoloRecorder;

public class InputReceiver implements Receiver{

	private List<MelodyNote> unfinishedNotes = new ArrayList<MelodyNote>();
	
	private InputWindow inputWindow;
	private SoloRecorder soloRecorder = null;
	
	public InputReceiver(InputWindow inputWindow) {
		this.inputWindow = inputWindow;
	}
	
	public void setRecorder(SoloRecorder soloRecorder) {
		this.soloRecorder = soloRecorder;
	}
	
	public void removeRecorder() {
		setRecorder(null);
	}
	
	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (message instanceof ShortMessage) {
			ShortMessage mes = (ShortMessage) message;
			
			if (soloRecorder != null)
				soloRecorder.addMidiEvent(mes);
			
			if (mes.getCommand() == ShortMessage.NOTE_ON) {
				MelodyNote note = new MelodyNote(mes.getData1(), mes.getData2(), System.currentTimeMillis(), -1L);
				this.unfinishedNotes.add(note);
			}
			
			if (mes.getCommand() == ShortMessage.NOTE_OFF) {
				for (MelodyNote note : unfinishedNotes) {
					if (note.key == mes.getData1()) {
						note.length = Math.max(0, System.currentTimeMillis() - note.start);
						unfinishedNotes.remove(note);
						inputWindow.addNote(note);
						break;
					}
				}
			}
		}
	}

	@Override
	public void close() {}
}
