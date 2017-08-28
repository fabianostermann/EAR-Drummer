package record;

import init.Streams;
import input.InputManager;
import input.InputReceiver;

import java.util.ArrayList;

import javax.sound.midi.ShortMessage;

import output.OutputManager;
import playback.Metronome;
import playback.MetronomeListener;
import record.Record.Event;


public class SoloRecorder implements MetronomeListener, Runnable{
	
	private InputManager inputManager;
	private OutputManager outputManager;
	private Metronome metronome;
	
	protected Record record;
	
	private boolean isPlaying = false;
		
	/* variables for playback in tick() */
	boolean foundFirstTick = false;
	Event event; // memorize last event
	PlaybackThread currentThread;
	
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
		
		if (isPlaying) {
			// initialize event object
			if (event == null) {
				Streams.recordOut.println("Initializing first event");
				record.rewind();
				event = record.nextEvent();
				new Thread(this).start();
			}
			
			// expecting tick event, skip any midi event
			while (event.getClass() != Record.TickEvent.class) {
				event = record.nextEvent();
				Streams.recordOut.println("Expected tick event, skipping " + event);
			}
			
			// wait for the next suitable tick
			// be careful here, if getTick() is higher than max ticks of metronome it hangs
			if (((Record.TickEvent) event).getTick() != metronome.getTick()) {
				Streams.recordOut.println("Skip tick=" + metronome.getTick());
				return;
			}
			
			long metronomeTickTimestamp = System.currentTimeMillis();
			long tickEventTimestamp = ((Record.TickEvent) event).getTimestamp();
			
			currentThread = new PlaybackThread(metronomeTickTimestamp, tickEventTimestamp, inputManager, outputManager);
				
			Streams.recordOut.println("Found suitable tick event " + event + ", metronome.tick=" + metronome.getTick());
			
			// playback of midi events
			event = record.nextEvent();
			while (event != null && event.getClass() == Record.MidiEvent.class) {
				currentThread.add((Record.MidiEvent)event);
				Streams.recordOut.println("Added " + event + " to thread events");
				event = record.nextEvent();
			}
			
			currentThread.start();
			
			if (event == null) {
				isPlaying = false;
				Streams.recordOut.println("Playback ended.");
			} else {
				Streams.recordOut.println("Tick() ended, should have next tick here: " + event);
			}
		}
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
			else // start playback synced by tick()
				isPlaying = true;
		else
			Streams.recordOut.println("Cannot playback record, still recording.");
	}
	
	/**
	 *  Plays back the current record in a thread
	 *  and syncronizes with metronome
	 */
	@Override
	public void run() {
		
		
		
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
