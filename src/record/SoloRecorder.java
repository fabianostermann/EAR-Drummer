package record;

import init.Settings;
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
	private Metronome metronome;
	
	private Record record;
	
	private boolean isPlaying = false;
		
	public SoloRecorder(InputManager inputManager, OutputManager outputManager, Metronome metronome) {
		
		this.inputManager = inputManager;
		this.outputManager = outputManager;
		this.metronome = metronome;
		
		if (inputManager.getReceiver() != null
				&& inputManager.getReceiver().getClass() == InputReceiver.class)
			((InputReceiver)inputManager.getReceiver()).setRecorder(this);
		else
			System.err.println("SoloRecorder has not found suitable inputReceiver.");
		
		metronome.addMetronomeListener(this);
	}
	
	@Override
	public void tick(Metronome metronome) {
		if (record != null)
			record.addTickEvent(metronome.getTick());
		
		/* TODO playback triggering here, syncs via tickEvents */
	}
	
	public void addMidiEvent(ShortMessage message) {
		if (record != null)
			record.addMidiEvent(message);
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
	
	/**
	 *  Plays back the current record in a thread
	 *  and syncronizes with metronome
	 */
	@Override
	public void run() {
		
		record.rewind();
		
		/*  TODO this idea does not work for syncing, use metronome trigger tick() instead */
//		Record.Event event;
//		Record.MidiEvent midiEvent;
//		
//		isPlaying = true;
//		long playbackTimer = 0;
//		long staticTimer = 0;
//		long startTimestamp = System.currentTimeMillis();
//		long roundStartTime = startTimestamp;
//		
//		while((event = record.nextEvent()) != null) {
//			
//			roundStartTime = System.currentTimeMillis();
//			
//			try {
//				Thread.sleep(Math.max(0L, event.getTimestamp() - playbackTimer));
//			} catch (InterruptedException e) { e.printStackTrace(); }
//			
//			if (event.getClass() == Record.MidiEvent.class) {
//				midiEvent = (Record.MidiEvent) event;
//				inputManager.getReceiver().send(midiEvent.getMidi(), -1);
//				outputManager.getReceiver().send(midiEvent.getMidi(), -1);
//			}
//			
//			if (event.getClass() == Record.TickEvent.class) {
//				
//			}
//			
//			playbackTimer += (System.currentTimeMillis() - roundStartTime);
//			staticTimer = System.currentTimeMillis() - startTimestamp;
//			
//			if (Settings.DEBUG)
//				Streams.recordOut.println("sT=" + staticTimer + ", pT=" + playbackTimer + ": " + event);			
//		}
//		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setRecord(Record record) {
		this.record = record;
	}
	
}
