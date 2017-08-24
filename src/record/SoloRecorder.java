package record;

import init.Streams;
import input.InputManager;
import input.InputReceiver;

import javax.sound.midi.ShortMessage;

import output.OutputManager;
import playback.Metronome;
import playback.MetronomeListener;

public class SoloRecorder implements MetronomeListener, Runnable{
	
	private InputManager inputManager;
	private OutputManager outputManager;
	
	private Record record;
	
	private long playbackTimer;
	private boolean isPlaying = false;
		
	public SoloRecorder(InputManager inputManager, OutputManager outputManager) {
		
		this.inputManager = inputManager;
		this.outputManager = outputManager;
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
		if (record!= null && !record.isRecording())
			if (isPlaying())
				Streams.recordOut.println("Connot playback record, already playing.");
			else // start playback thread
				new Thread(this).start();
		else
			System.err.println("Cannot playback record, still recording.");
	}
	
	@Override
	public void run() {
//		inputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
//		outputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
		
		record.rewind();
		
		playbackTimer = 0;
		long startTime = System.currentTimeMillis();
		
		Record.Event event;
		Record.MidiEvent midiEvent;
		isPlaying = true;
		
		while((event = record.nextEvent()) != null) {
			try {
				Thread.sleep(Math.max(0L, event.getTimestamp() - playbackTimer));
			} catch (InterruptedException e) { e.printStackTrace(); }
			
			System.out.println(playbackTimer + ": " + event);
			if (event.getClass() == Record.MidiEvent.class) {
				midiEvent = (Record.MidiEvent) event;
				inputManager.getReceiver().send(midiEvent.getMidi(), -1);
				outputManager.getReceiver().send(midiEvent.getMidi(), -1);
			}
			
			playbackTimer = System.currentTimeMillis() - startTime;
		}
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setRecord(Record record) {
		this.record = record;
	}
	
}
