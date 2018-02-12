package bass;

import gui.LoadSaveable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Observable;

import output.BassGenerator;
import output.GM1;

import playback.Metronome;
import playback.MetronomeListener;

public class SimpleBassist extends Observable implements MetronomeListener, LoadSaveable {

	public static final Object BAR_CHANGED = new Object();
	public static final Object TABLE_CHANGED = new Object();
	
	private BassGenerator bassGenerator;
	
	private int barCount = -1;
	private ChordLib.ChordTable table = new ChordLib.ChordTable();
	private int midiVolume = 100;

	public static final int HIGHEST_NOTE = GM1.Gb_1;
	
	public SimpleBassist(BassGenerator bassGenerator) {
		this.bassGenerator = bassGenerator;
		
		table = ChordLib.interpretChordTableString("");
	}
	
	@Override
	public void tick(Metronome metronome) {
		if (metronome.getTick() == 0) {
			increaseBarCount();
			setChanged();
			notifyObservers(BAR_CHANGED);
		}
		
		int midi = -1;
		
		// Bossa Nova
		switch (metronome.getTick()) {
		case 0: midi = root();break;
		case 3: midi = root() +chord()[2];break;
		case 4: midi = root() +chord()[2];break;
		case 7: midi = nextRoot();break;
		default: return;
		}
		while (midi > HIGHEST_NOTE)
			midi -= 12;
		
		bassGenerator.play(midi, midiVolume);
	}

	private int root() {
		return 120+table.chordRoots[getBarCount()];
	}
	
	private int[] chord() {
		return table.chordNotes[getBarCount()];
	}
	
	private int nextRoot() {
		return 120+table.chordRoots[(getBarCount()+1) % table.chordRoots.length];
	}
	
	private void increaseBarCount() {
		barCount++;
		if (barCount >= table.chordRoots.length)
			barCount = 0;
	}

	@Override
	public void loadFromFile(RandomAccessFile raf) throws IOException {
		// dummy code, implement loading of chord sheet
		String line = "";
		String code = "";
		while ((line = raf.readLine()) != null) {
			code += line+"\n";
		}
		this.table = ChordLib.interpretChordTableString(code);
		barCount = -1;
		setChanged();
		notifyObservers(TABLE_CHANGED);
	}

	@Override
	public void saveToFile(RandomAccessFile raf) throws IOException {
		throw new UnsupportedOperationException("Not implemented.");
	}
	
	public int getBarCount() {
		return Math.max(0, this.barCount);
	}
	
	public ChordLib.ChordTable getChordTable() {
		return this.table;
	}
	
	public int getMidiVolume() {
		return this.midiVolume;
	}
	
	public void setMidiVolume(int volume) {
		this.midiVolume = volume;
	}
}
