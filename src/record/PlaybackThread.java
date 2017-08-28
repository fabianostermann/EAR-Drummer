package record;

import init.Streams;
import input.InputManager;

import java.util.LinkedList;
import java.util.ListIterator;

import output.OutputManager;
import record.Record.MidiEvent;

public class PlaybackThread extends Thread {

	private LinkedList<Record.MidiEvent> midiEvents = new LinkedList<>();
	private long metronomeTickTimestamp;
	private long tickEventTimestamp;

	private InputManager inputManager;
	private OutputManager outputManager;
	
	public PlaybackThread(long metronomeTickTimestamp, long tickEventTimestamp,
			InputManager inputManager, OutputManager outputManager) {
		this.metronomeTickTimestamp = metronomeTickTimestamp;
		this.tickEventTimestamp = tickEventTimestamp;
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
				long timeFromLastMetronomeTick = midiEvent.getTimestamp() - tickEventTimestamp;
				long timeForPlayback = metronomeTickTimestamp + timeFromLastMetronomeTick;
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
