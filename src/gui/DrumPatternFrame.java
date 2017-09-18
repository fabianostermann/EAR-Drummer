package gui;

import genetic.DrumPattern;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Observable;
import java.util.Observer;

import playback.Metronome;
import playback.MetronomeListener;
import playback.PatternPlayer;


@SuppressWarnings("serial")
public class DrumPatternFrame extends ManagedFrame implements MetronomeListener, Observer, LoadSaveable {
	
	private DrumPattern pattern;
	
	private boolean editable;
	
	public DrumPatternFrame(DrumPattern pattern, Metronome metronome, boolean editable) {
		
		this.pattern = pattern;
		this.setEditable(editable);
		
		metronome.addMetronomeListener(this);
		
		if (this.isEditable())
			this.setTitle("DrumPattern Init");
		else
			this.setTitle("Current DrumPattern");
		
		this.initGUI();
		this.pack();
		
		// must be repeated because patternPane is now initialized
		this.setEditable(editable);
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}
	
	//Window components
	private DrumPatternPane patternPane;
	private LoadSavePanel loadSavePanel;
	
	private void initGUI() {
		
		this.patternPane = new DrumPatternPane(this.pattern);
		this.getContentPane().add(patternPane);
		
		if (this.isEditable()) {
			this.loadSavePanel = new LoadSavePanel(this, "patterns", "pattern1");
			this.getContentPane().add(loadSavePanel, BorderLayout.NORTH);
		}
	}
	
	@Override
	public void tick(Metronome metronome) {
		this.patternPane.highlightColumn(metronome.getTick());
	}
	
	public void setEditable(boolean b) {
		this.editable = b;
		if (patternPane != null)
			patternPane.setEditable(b);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	public void setPattern(DrumPattern pattern) {
		this.pattern = pattern;
		patternPane.setPattern(pattern);
	}
	
	@Override
	public void update(Observable o, Object obj) {
		if (o instanceof PatternPlayer) {
			PatternPlayer patternPlayer = (PatternPlayer) o;
			
			this.setPattern(patternPlayer.getPattern());
		}
	}

	@Override
	public void loadFromFile(RandomAccessFile raf) throws IOException {
		String line = "";
		int instrument = 0;
		while ((line = raf.readLine()) != null) {
			String[] singlePattern = line.split(":");
			for (int i = 0; i < Math.min(singlePattern.length, pattern.getTicks()); i++)
				pattern.set(i, instrument, Integer.parseInt(singlePattern[i]));
			instrument++;
		}
		patternPane.setPattern(this.pattern);
	}

	@Override
	public void saveToFile(RandomAccessFile raf) throws IOException {
		for (int i = 0; i < pattern.getInstruments(); i++) {
			for (int j = 0; j < pattern.getTicks(); j++) {
				raf.writeBytes(pattern.get(j,i)+":");
			}
			raf.writeBytes("\n");
		}
	}

	
	
}

