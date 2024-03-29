package init;

import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;

public class Settings {

	public static final String TITLE = "EAR DRUMMER";
	public static final String AUTHOR = "Fabian Ostermann";
	public static final String VERSION = "v0.1";
	
	public static boolean DEBUG = false;
	
	public enum FitnessVersion {
	    RuleBased, CombinationBased
	}
	public static FitnessVersion FITNESS_VERSION = FitnessVersion.RuleBased;

	public static final String CONFIG_FOLDER = "config";
	
	//Metronome default settings
	public static int TICKS = 8;
	public static int MIN_TPM = 100;
	public static int TPM = 200;
	public static int MAX_TPM = 700;
	public static float SWING = 0.67f;
	
	//MIDI devices settings
	public static int DEFAULT_INPUT_DEVICE_NUMBER = 1;
	public static Info getDefaultInputDevice() {
		return MidiSystem.getMidiDeviceInfo()[DEFAULT_INPUT_DEVICE_NUMBER];
	}
	
	public static int DEFAULT_OUTPUT_DEVICE_NUMBER = 0;
	public static Info getDefaultOutputDevice() {
		return MidiSystem.getMidiDeviceInfo()[DEFAULT_OUTPUT_DEVICE_NUMBER];
	}
	
}
