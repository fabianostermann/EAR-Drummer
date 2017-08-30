package record;

import java.io.IOException;
import java.io.RandomAccessFile;

import gui.LoadSaveable;
import init.Streams;
import input.InputManager;
import input.InputReceiver;

import javax.sound.midi.ShortMessage;

import output.OutputManager;
import playback.Metronome;
import playback.MetronomeListener;
import record.Record.Event;


public class SoloRecorder implements MetronomeListener, LoadSaveable {
	
	private InputManager inputManager;
	private OutputManager outputManager;
	private Metronome metronome;
	
	private Record record;
	
	private boolean isPlaying = false;
		
	/** variables for playback in tick() */
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
		
		this.metronome.addMetronomeListener(this);
	}
	
	@Override
	public void tick(Metronome metronome) {
		if (record != null)
			record.addTickEvent(metronome.getTick(), metronome.getSettingsClone());
		
		if (record == null || record.size() <= 0) {
			isPlaying = false;
			Streams.recordOut.println("Cannot play back, record is empty.");
		}
		if (isPlaying) {
			// initialize event object
			if (event == null) {
				Streams.recordOut.println("Initializing first event");
				record.rewind();
				event = record.nextEvent();
			}
			
			// expecting tick event, skip any midi event
			while (event.getClass() != Record.TickEvent.class) {
				event = record.nextEvent();
				Streams.recordOut.println("Expected tick event, skipping " + event);
			}
			
			// wait for the next suitable tick
			// be careful here, if getTick() is higher than max ticks of metronome it hangs
			if (((Record.TickEvent) event).getTick() >= metronome.getTicks()) {
				throw new IllegalArgumentException("Ticks of metronome are smaller than expected tick, dead loop!");
			}
			if (((Record.TickEvent) event).getTick() != metronome.getTick()) {
				Streams.recordOut.println("Skip tick=" + metronome.getTick());
				return;
			}
			
			metronome.setSettings(((Record.TickEvent) event).getSettings());
			
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
			else // start playback synchronized by tick()
				isPlaying = true;
		else
			Streams.recordOut.println("Cannot playback record, still recording.");
	}
	
	public void stopPlayback() {
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setRecord(Record record) {
		this.record = record;
	}

	@Override
	public void loadFromFile(RandomAccessFile raf) throws IOException {
		stopPlayback();
		if (!record.isRecording()) {
			this.record = new Record();
			this.record.loadFromFile(raf);
		}
	}

	@Override
	public void saveToFile(RandomAccessFile raf) throws IOException {
		if (this.record != null)
			this.record.saveToFile(raf);
		else
			Streams.recordOut.println("No record to save.");
	}
	
}
