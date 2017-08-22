package record;

import init.Settings;
import init.Streams;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;

public class Record {

	public final long startTimestamp = System.currentTimeMillis();

	private ArrayList<Event> events = new ArrayList<>();
	private boolean isRecording = true;
	
	public Record() {}
	
	public Record(RandomAccessFile raf) {
		/** TODO loading record */
		endRecord();
	}
	
	public void addEvent(Event event) {
		if (isRecording()) {
			events.add(event);
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
	}
	
	public void saveRecord(RandomAccessFile raf) {
		/** TODO saving record */
	}
	
	public static class Event implements Comparable<Event> {
		
		private long timestamp;
		private Object object;
		
		public Event(long timestamp, long startTimestamp, Object object) {
			this.timestamp = timestamp - startTimestamp;
			this.object = object;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
		
		public Object getObject() {
			return object;
		}
		
		@Override
		public int compareTo(Event other) {
			return (int)(this.getTimestamp() - other.getTimestamp());
		}
		
		@Override
		public String toString() {
			return "[" + timestamp + " - " + object.toString() + "]";
		}
	}

}
