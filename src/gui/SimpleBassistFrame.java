package gui;

import init.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bass.SimpleBassist;

public class SimpleBassistFrame extends ManagedFrame implements Observer {
	
	private SimpleBassist bassist;

	public SimpleBassistFrame(SimpleBassist bassist) {

		if (bassist == null)
			throw new NullPointerException();

		this.bassist = bassist;
		loadSavePanel = new LoadSavePanel(bassist, "sheets", "sheet1");

		this.setTitle("Simple Bassist");

		this.initGUI();
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		bassist.addObserver(this);
	}
	
	// GUI elements
	private final String BARLINE_CHAR = "|";
	private final int NUMBER_OF_CHORDS_IN_A_ROW = 4;
	private JLabel[] chordNamesLabels;
	private JPanel chordTablePane = new JPanel(new GridLayout(0, NUMBER_OF_CHORDS_IN_A_ROW*2+1));
	
	private LoadSavePanel loadSavePanel;

	private JButton resetButton = ImageLoader.createButton("Reset");
	private JLabel statusLabel = new JLabel("no status");
	private JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	private void initGUI() {
		
		makeChordTable();
		this.getContentPane().add(chordTablePane, BorderLayout.CENTER);
		
		this.getContentPane().add(loadSavePanel, BorderLayout.NORTH);
		
		resetButton.setEnabled(false);
		settingsPanel.add(resetButton);
		settingsPanel.add(statusLabel);
		this.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
	}
	
	private void makeChordTable() {
		String[] chordNames = this.bassist.getChordTable().chordNames;
		
		chordTablePane.removeAll();
		chordNamesLabels = new JLabel[chordNames.length];
		
		if (chordNames.length == 0) {
			statusLabel.setText("Chord Table is empty");
		}
		else for (int i = 0; i < chordNames.length; i++) {
			if (i % NUMBER_OF_CHORDS_IN_A_ROW == 0) {
				JPanel beginBarlinePane = new JPanel(new FlowLayout());
				beginBarlinePane.add(new JLabel(BARLINE_CHAR));
				chordTablePane.add(beginBarlinePane);
			}
			chordNamesLabels[i] = new JLabel(chordNames[i]);
			chordTablePane.add(chordNamesLabels[i]);
			JPanel barlinePane = new JPanel(new FlowLayout());
			barlinePane.add(new JLabel(BARLINE_CHAR));
			chordTablePane.add(barlinePane);
			statusLabel.setText("Chord Table is loaded.");
		}
		this.pack();
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable != bassist)
			return;
		
		if (arg == SimpleBassist.TABLE_CHANGED) {
			makeChordTable();
		}
		
		for (int i = 0; i < chordNamesLabels.length; i++) {
			if (i == bassist.getBarCount())
				chordNamesLabels[i].setForeground(Color.RED);
			else
				chordNamesLabels[i].setForeground(Color.BLACK);
		}
	}

}
