package record;

import input.InputReceiver;

import javax.sound.midi.ShortMessage;

import playback.Metronome;
import playback.MetronomeListener;

public class SoloRecorder implements MetronomeListener, Runnable{

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
			record.addEvent(new Record.TickEvent(System.currentTimeMillis(), record.startTimestamp, metronome.getTick()));
	}
	
	public void addMidiEvent(ShortMessage message) {
		if (record != null)
			record.addEvent(new Record.MidiEvent(System.currentTimeMillis(), record.startTimestamp, message));
	}
	
	public void startNewRecording() {
		record = new Record();
	}
	
	public void stopRecording() {
		if (record != null) record.endRecord();
	}
	
	public void playbackRecord() {
		if (!record.isRecording()) {
			// start playback thread
			new Thread(this).start();
		}
		else {
			System.err.println("Cannot playback record, still recording.");
		}
	}
	
	@Override
	public void run() {
		/* TODO inputManager must be called to get Receiver and inputManager must manage recording source changed for recording */
		// TODO playback via inputReceiver
		
//		inputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
//		outputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
		
		Record.Event event;
		while((event = record.nextEvent()) != null) {
			System.out.print(event + ", ");
		}
		System.out.println();
	}
	
	public void setRecord() {}
	
}
