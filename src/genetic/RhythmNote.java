package genetic;


public class RhythmNote {
	
	//Define instruments:
	public static final int BASS = 0;
	public static final int HIHAT = 1;
	public static final int SNARE = 2;
	public static final int RIDE = 3;
	public static final int LOWTOM = 4;
	public static final int HIGHTOM = 5;
	public static final int NUMBER_OF_DRUMS = 6;
	
	//IMPORTANT: change names when changing instruments above!
	public static final String[] drumNames = {"bass","hihat","snare","ride","lowtom","hightom"};
	
	public static final int MIDI_BASS = 35;
	public static final int MIDI_HIHAT_CLOSED = 42;
	public static final int MIDI_HIHAT_OPEN = 46;
	public static final int MIDI_HIHAT_PEDAL = 44;
	public static final int MIDI_SNARE = 40;
	public static final int MIDI_SIDE_STICK = 37;
	public static final int MIDI_RIDE = 51;
	public static final int MIDI_RIDE_BELL = 53;
	public static final int MIDI_CRASH = 49;
	public static final int MIDI_CHINA = 52;
	public static final int MIDI_LOWTOM = 43;
	public static final int MIDI_HIGHTOM = 48;
	
	private final int instrument;
	private final int volume;
	
	//TODO make the intelligent drum choice disableable via gui
	private static boolean intelligentDrum = true;
	
	public RhythmNote(int instrument, int volume) {
		this.instrument = instrument;
		this.volume = volume;
	}
	
	public int getInstrument() {
		return instrument;
	}
	

	public int getMidi() {
		switch(instrument) {
		case BASS: return MIDI_BASS;
		case HIHAT:
			if (intelligentDrum && this.volume < 70)
				return MIDI_HIHAT_PEDAL;
			else if (!intelligentDrum || this.volume <= 120)
				return MIDI_HIHAT_CLOSED;
			else
				return MIDI_HIHAT_OPEN;
		case SNARE:
			if (intelligentDrum && this.volume < 70)
				return MIDI_SIDE_STICK;
			else
				return MIDI_SNARE;
		case RIDE:
			if (!intelligentDrum || this.volume < 110)
				return MIDI_RIDE;
			else if (this.volume < 122)
				return MIDI_RIDE_BELL;
			else if (this.volume < 127)
				return MIDI_CRASH;
			else
				return MIDI_CHINA;
		case LOWTOM: return MIDI_LOWTOM;
		case HIGHTOM: return MIDI_HIGHTOM;
		}
		throw new IllegalArgumentException("instrument "+instrument+" undefined");
	}
	
	public static void enableIntelligentDrum(boolean b) {
		RhythmNote.intelligentDrum = b;
	}
	
	public static boolean isIntelligentDrumEnabled() {
		return RhythmNote.intelligentDrum;
	}
	
	public int getVolume() {
		return volume;
	}
	
	@Override
	public String toString() {
		return "[instrument="+instrument+", volume="+volume+"]";
	}

}
