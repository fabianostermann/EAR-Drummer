package record;

import gui.LoadSaveable;
import init.Settings;
import init.Streams;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import playback.Metronome;

public class Record implements LoadSaveable {

	public long startTimestamp;

	private LinkedList<Event> events = new LinkedList<>();
	private ListIterator<Event> iterator;
	
	private boolean isRecording = false;
	private boolean recordEnded = false;
	
	/** Creates a new record that is recording when next bar is reached
	 *  (trigger is metronome.getTick()==0) */
	public Record() {}
	
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
		Streams.recordOut.println("New event recorded: " + event);
	}
	
	public boolean isRecording() {
		return isRecording;
	}
	
	public void endRecord() {
		if (isRecording()) { 
			Collections.sort(events);
			Streams.recordOut.println("Recording ended, record was sorted: " + events);
		}
		this.rewind();
		
		isRecording = false;
		recordEnded = true;
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
	
	public int size() {
		return events.size();
	}
	
	public void loadFromFile(RandomAccessFile raf) throws IOException {
		this.endRecord();
		events.clear();
		
		// load record analog to saveToFile()
		String line;
		int lineNumber = 0;
		while ((line = raf.readLine()) != null) {
			lineNumber++;
			try {
			String[] parts = line.split(":");
			if (parts.length > 0)
				if (parts[0].equals("TICK")) {
					long timestamp = Long.parseLong(parts[1]);
					int tick = Integer.parseInt(parts[2]);
					int tpm = Integer.parseInt(parts[3]);
					float shuffle = Float.parseFloat(parts[4]);
					Metronome.Settings settings = (new Metronome(Settings.TICKS)).new Settings(tpm, shuffle);
					events.add(new TickEvent(timestamp, 0, tick, settings));
				}
				else if (parts[0].equals("MIDI")) {
					long timestamp = Long.parseLong(parts[1]);
					int command;
					if (parts[2].equals("NOTE_ON"))
						command = ShortMessage.NOTE_ON;
					else if (parts[2].equals("NOTE_OFF"))
						command = ShortMessage.NOTE_OFF;
					else
						command = Integer.parseInt(parts[2]);
					int channel = Integer.parseInt(parts[3]);
					int data1 = Integer.parseInt(parts[4]);
					int data2 = Integer.parseInt(parts[5]);
					ShortMessage message = new ShortMessage(command, channel, data1, data2);
					events.add(new MidiEvent(timestamp, 0, message));
				}
			} catch (Exception e) {
				Streams.recordOut.println("Error while reading input at line " + lineNumber);
				e.printStackTrace();
			}
		}
		
		Streams.recordOut.println("Loaded record " + this + ": " + events);
	}

	public void saveToFile(RandomAccessFile raf) throws IOException {
		this.endRecord();
		
		// save record analog to loadToFile()
		TickEvent tickEvent;
		MidiEvent midiEvent;
		for (Event event : events) {
			if (event.getClass() == Record.TickEvent.class) {
				tickEvent = (TickEvent) event;
				raf.writeBytes("TICK:");
				raf.writeBytes(tickEvent.getTimestamp() + ":");
				raf.writeBytes(tickEvent.getTick() + ":");
				raf.writeBytes(tickEvent.getSettings().tpm + ":");
				raf.writeBytes(tickEvent.getSettings().shuffle + ":");
				raf.writeBytes("\n");
			}
			else if (event.getClass() == Record.MidiEvent.class) {
				midiEvent = (MidiEvent) event;
				raf.writeBytes("MIDI:");
				raf.writeBytes(midiEvent.getTimestamp() + ":");
				if (midiEvent.getMidi().getCommand() == ShortMessage.NOTE_ON)
					raf.writeBytes("NOTE_ON" + ":");
				else if (midiEvent.getMidi().getCommand() == ShortMessage.NOTE_OFF)
					raf.writeBytes("NOTE_OFF" + ":");
				else
					raf.writeBytes(midiEvent.getMidi().getCommand() + ":");
				raf.writeBytes(midiEvent.getMidi().getChannel() + ":");
				raf.writeBytes(midiEvent.getMidi().getData1() + ":");
				raf.writeBytes(midiEvent.getMidi().getData2() + ":");
				raf.writeBytes("\n");
			} 
			else {
				System.err.println("Event " + event + "is not saveable!");
			}
		}
		
		Streams.recordOut.println("Saved record " + this + ": " + events);
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
