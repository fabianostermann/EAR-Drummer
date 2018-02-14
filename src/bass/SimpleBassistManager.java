package bass;

import gui.LoadSaveable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Observable;

import output.BassGenerator;
import output.GM1;

import playback.Metronome;
import playback.MetronomeListener;

public class SimpleBassistManager extends Observable implements MetronomeListener, LoadSaveable {

	public static final Object BAR_CHANGED = new Object();
	public static final Object TABLE_CHANGED = new Object();
	
	private BassGenerator bassGenerator;
	
	private SimpleBassist bassist = SimpleBassist.list[0];
	
	private int barCount = -1;
	private ChordLib.ChordTable table = new ChordLib.ChordTable();
	private int midiVolume = 100;
	
	public SimpleBassistManager(BassGenerator bassGenerator) {
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
		
		if (getChordTable().chordRoots.length <= 0)
			return;
		
		Integer midi = bassist.nextNote(
				getBarCount(), metronome.getTick(),
				metronome.getTicks(), getChordTable());
		if (midi != null)
			bassGenerator.play(midi, midiVolume);
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
