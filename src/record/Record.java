package record;

import init.Settings;
import init.Streams;

import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.sound.midi.ShortMessage;

import playback.Metronome;

public class Record {

	public long startTimestamp;

	private LinkedList<Event> events = new LinkedList<>();
	private ListIterator<Event> iterator;
	
	private boolean isRecording = false;
	private boolean recordEnded = false;
	
	/** Creates a new record that is recording when next bar is reached
	 *  (trigger is metronome.getTick()==0) */
	public Record() {}
	
	public Record(RandomAccessFile raf) {
		/* TODO loading record */
		endRecord();
	}
	
	public void addMidiEvent(ShortMessage message) {
		if (isRecording())
			this.addEvent(new Record.MidiEvent(System.currentTimeMillis(), startTimestamp, message));
	}
	
	public void addTickEvent(int tick, Metronome.Settings settings) {
		if (isRecording())
			this.addEvent(new Record.TickEvent(System.currentTimeMillis(), startTimestamp, tick, settings));
		else
			if (!recordEnded && tick == 0) {
			// wait with recording until tick 0 comes up
			// then set startTimestamp to current time
			isRecording = true;
			startTimestamp = System.currentTimeMillis();
			this.addEvent(new Record.TickEvent(startTimestamp, startTimestamp, tick, settings));
			return;
		}
	}
	
	private void addEvent(Event event) {
		events.addLast(event);
		if (Settings.DEBUG) 
			Streams.recordOut.println("New event recorded: " + event);
	}
	
	public boolean isRecording() {
		return isRecording;
	}
	
	public void endRecord() {
		isRecording = false;
		recordEnded = true;
		Collections.sort(events);
		
		if (Settings.DEBUG) 
			Streams.recordOut.println("Recording ended, record was sorted: " + events);
	}
	
	public void rewind() {
		iterator = events.listIterator();
	}
	
	public Event nextEvent() {
		if (iterator.hasNext())
			return iterator.next();
		else
			return null;
	}
	
	public void saveRecord(RandomAccessFile raf) {
		/* TODO saving record */
	}
	
	public static class Event implements Comparable<Event> {
		
		private long timestamp;
		private Object object;
		
		private Event(long timestamp, long startTimestamp, Object object) {
			this.timestamp = timestamp - startTimestamp;
			this.object = object;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
		
		protected Object getObject() {
			return object;
		}
		
		@Override
		public int compareTo(Event other) {
			return (int)(this.getTimestamp() - other.getTimestamp());
		}
	}

	public static class MidiEvent extends Event {

		public MidiEvent(long timestamp, long startTimestamp, ShortMessage message) {
			super(timestamp, startTimestamp, message);
		}
		
		public ShortMessage getMidi() {
			return (ShortMessage) getObject();
		}

		@Override
		public String toString() {
			String command = getMidi().getCommand() == ShortMessage.NOTE_ON ? "NOTE_ON" : "NOTE_OFF";
			String key = ""+getMidi().getData1();
			String vel = ""+getMidi().getData2();
			return "(" + getTimestamp() + "|midi="+command+",key="+key+",vel="+vel+ ")";
		}
	}
	
	public static class TickEvent extends Event {

		Metronome.Settings settings;
		
		public TickEvent(long timestamp, long startTimestamp, int tick, Metronome.Settings settings) {
			super(timestamp, startTimestamp, tick);
			this.settings = settings;
		}
		
		public int getTick() {
			return (int) getObject();
		}
		
		public Metronome.Settings getSettings() {
			return this.settings;
		}

		@Override
		public String toString() {
			return "(" + getTimestamp() + "|tick=" + getTick() + ",settings=" + getSettings() + ")";
		}
	}	
	
}
