package record;

import init.Settings;
import init.Streams;

import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.sound.midi.ShortMessage;

public class Record {

	public final long startTimestamp = System.currentTimeMillis();

	private LinkedList<Event> events = new LinkedList<>();
	private ListIterator<Event> iterator;
	
	private boolean isRecording = true;
	
	public Record() {}
	
	public Record(RandomAccessFile raf) {
		/** TODO loading record */
		endRecord();
	}
	
	public void addEvent(Event event) {
		if (isRecording()) {
			events.addLast(event);
			if (Settings.DEBUG) 
				Streams.recordOut.println("New record event: " + event);
		}
	}
	
	public boolean isRecording() {
		return isRecording;
	}
	
	public void endRecord() {
		isRecording = false;
		Collections.sort(events);
		rewind();
		
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
		/** TODO saving record */
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

		public TickEvent(long timestamp, long startTimestamp, int tick) {
			super(timestamp, startTimestamp, tick);
		}
		
		public int getTick() {
			return (int) getObject();
		}

		@Override
		public String toString() {
			return "(" + getTimestamp() + "|tick=" + getTick() + ")";
		}
	}	
	
}
