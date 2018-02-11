package record;

import init.Streams;
import input.InputManager;

import java.util.LinkedList;
import java.util.ListIterator;

import output.OutputManager;
import record.Record.MidiEvent;
import record.Record.TickEvent;

public class PlaybackThread extends Thread {

	/* TODO event must be accelerated depending on current metronome settings */
	
	private LinkedList<Record.MidiEvent> midiEvents = new LinkedList<>();
	
	private long initTimestamp = System.currentTimeMillis();
	private TickEvent tickEvent;
	
	private InputManager inputManager;
	private OutputManager outputManager;
	
	/**
	 * Creates a new Playback thread with following attributes
	 * @param tickEvent : the event of the tick that this playback thread has to play back
	 * @param inputManager
	 * @param outputManager
	 */
	public PlaybackThread(TickEvent tickEvent,
			InputManager inputManager, OutputManager outputManager) {
		this.tickEvent = tickEvent;
		this.inputManager = inputManager;
		this.outputManager = outputManager;
	}
	
	public void add(Record.MidiEvent event) {
		midiEvents.add(event);
	}
	
	@Override
	public void run() {
		
		if (midiEvents.isEmpty())
			return;
		
		ListIterator<MidiEvent> iter = midiEvents.listIterator();
		Record.MidiEvent midiEvent = iter.next();
		
		Streams.recordOut.println("New Thread " + this + " starts.");
		
		while (true) {
			
			try {
				long timeFromLastMetronomeTick = midiEvent.getTimestamp() - tickEvent.getTimestamp();
				long timeForPlayback = initTimestamp + timeFromLastMetronomeTick;
				Thread.sleep(Math.max(0L, timeForPlayback -  System.currentTimeMillis() ));
			} catch (InterruptedException e) { e.printStackTrace(); }
			
			inputManager.getReceiver().send(midiEvent.getMidi(), -1);
			outputManager.getReceiver().send(midiEvent.getMidi(), -1);
			Streams.recordOut.println("Thread " + this + " is playing: " + midiEvent);
			
			if (iter.hasNext())
				midiEvent = iter.next();
			else {
				Streams.recordOut.println("Thread " + this + " ends.");
				return;
			}
		}
	}
}
