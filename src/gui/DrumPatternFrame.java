package gui;

import genetic.DrumPattern;

import java.util.Observable;
import java.util.Observer;

import playback.Metronome;
import playback.MetronomeListener;
import playback.PatternPlayer;


@SuppressWarnings("serial")
public class DrumPatternFrame extends ManagedFrame implements MetronomeListener, Observer {
	
	private DrumPattern pattern;
	
	private boolean editable;
	
	public DrumPatternFrame(DrumPattern pattern, Metronome metronome, boolean editable) {
		
		this.pattern = pattern;
		
		metronome.addMetronomeListener(this);
		
		this.setTitle("DrumPattern Init");
		
		this.initGUI();
		this.pack();
		
		this.setEditable(editable);
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}
	
	//Window components
	private DrumPatternPane patternPane;
	
	private void initGUI() {
		
		this.patternPane = new DrumPatternPane(this.pattern);
		this.getContentPane().add(patternPane);
	}
	
	@Override
	public void tick(Metronome metronome) {
		this.patternPane.highlightColumn(metronome.getTick());
	}
	
	public void setEditable(boolean b) {
		this.editable = b;
		patternPane.setEditable(b);
		
		if (b)
			this.setTitle("DrumPattern Init");
		else
			this.setTitle("Current DrumPattern");
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

	
	
}

