package bass;

import gui.LoadSaveable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Observable;

import playback.Metronome;
import playback.MetronomeListener;

public class SimpleBassist extends Observable implements MetronomeListener, LoadSaveable {

	public static final Object TICK_CHANGED = new Object();
	public static final Object TABLE_CHANGED = new Object();
	
	private int barCount = -1;
	private ChordLib.ChordTable table = new ChordLib.ChordTable();
	
	public SimpleBassist() {
		table = ChordLib.interpretChordTableString("");
	}
	
	@Override
	public void tick(Metronome metronome) {
		if (metronome.getTick() == 0)
			increaseBarCount();
		
		setChanged();
		notifyObservers(TICK_CHANGED);
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
		return barCount;
	}
	
	public ChordLib.ChordTable getChordTable() {
		return table;
	}
}
