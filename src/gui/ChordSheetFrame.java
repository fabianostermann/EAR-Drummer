package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import playback.ChordSheetManager;

public class ChordSheetFrame extends ManagedFrame {

	ChordSheetManager chordSheetManager;
	
	public ChordSheetFrame(ChordSheetManager chordSheetManager) {
		this.chordSheetManager = chordSheetManager;
		
		this.setTitle("Chord Sheet");
		
		this.initGUI();
		this.pack();
//		this.setResizable(false);

		this.setLocationByPlatform(true);
		this.setVisible(true);
	}
	
	private JTextArea sheetArea = new JTextArea("sheet area");

	private void initGUI() {
		sheetArea.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.LIGHT_GRAY));
		this.add(sheetArea, BorderLayout.CENTER);
	}
	
}
