package record;

import input.InputReceiver;

import javax.sound.midi.ShortMessage;

import playback.Metronome;
import playback.MetronomeListener;

public class SoloRecorder implements MetronomeListener{

	private Metronome metronome;
	private InputReceiver inputReceiver;
	
	private Record record;
	
	public SoloRecorder(Metronome metronome, InputReceiver inputReceiver) {
		
		this.metronome = metronome;
		this.inputReceiver = inputReceiver;
		
		metronome.addMetronomeListener(this);
		inputReceiver.setRecorder(this);
	}

	@Override
	public void tick(Metronome metronome) {
		if (record != null)
			record.addEvent(new Record.Event(System.currentTimeMillis(), record.startTimestamp, metronome.getTick()));
	}
	
	public void addMidiEvent(ShortMessage message) {
		if (record != null)
			record.addEvent(new Record.Event(System.currentTimeMillis(), record.startTimestamp, message));
	}
	
	public void startNewRecording() {
		record = new Record();
	}
	
	public void stopRecording() {
		if (record != null) record.endRecord();
	}
	
	public void playbackRecord() {
		if (!record.isRecording()) {
			// TODO implement playback
		}
	}
	
	public void setRecord() {}
	
}
