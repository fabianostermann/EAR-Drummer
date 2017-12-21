package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	
	private JTextArea sheetArea = new JTextArea("| c:m7 | f:m7 | c:m7 | % |\n| f:m7 | % | c:m7 | % |\n| g:7 | f:m7 | c:m7 | g:7 |");
	private JButton editSheetButton = new JButton("Edit");
	private JButton buildSheetButton = new JButton("Build");
	
	private void initGUI() {
		sheetArea.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.LIGHT_GRAY));
		this.add(sheetArea, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		editSheetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSheetButtonClicked();
			}
		});
		buildSheetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildSheetButtonClicked();
			}
		});
		bottomPanel.add(editSheetButton);
		bottomPanel.add(buildSheetButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void editSheetButtonClicked() {
		sheetArea.setEnabled(true);
	}
	
	private void buildSheetButtonClicked() {
		String errorString = chordSheetManager.interpret(sheetArea.getText());
		if (errorString == ChordSheetManager.INTERPRETING_SUCCESSFUL) {
			sheetArea.setEnabled(false);
		} else {
			JOptionPane.showMessageDialog(this, errorString, "Interpretation failed", JOptionPane.ERROR_MESSAGE);
		}
	}
}
