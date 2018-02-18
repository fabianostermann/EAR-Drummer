package gui;

import init.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bass.SimpleBassist;
import bass.SimpleBassistManager;

@SuppressWarnings("serial")
public class SimpleBassistManagerFrame extends ManagedFrame implements Observer {
	
	private SimpleBassistManager bassistManager;

	public SimpleBassistManagerFrame(SimpleBassistManager bassistManager) {

		if (bassistManager == null)
			throw new NullPointerException();

		this.bassistManager = bassistManager;
		loadSavePanel = new LoadSavePanel(bassistManager, "sheets", "sheet1");

		this.setTitle("Simple Bassist");

		this.initGUI();
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(false);
		
		bassistManager.addObserver(this);
	}
	
	// GUI elements
	private final String BARLINE_CHAR = "|";
	private final int NUMBER_OF_CHORDS_IN_A_ROW = 4;
	private JLabel[] chordNamesLabels;
	private JPanel chordTablePane = new JPanel(new GridLayout(0, NUMBER_OF_CHORDS_IN_A_ROW*2+1));
	
	private LoadSavePanel loadSavePanel;

	
	private JPanel volumePane = new JPanel();
	private JSlider volumeSlider;
	private JLabel volumeLabel = new JLabel();
	
	private JButton resetButton = ImageLoader.createButton("Reset");
	private JLabel statusLabel = new JLabel("no status");
	private JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	private JComboBox<SimpleBassist> boxBassists;
	
	private void initGUI() {
		
		makeChordTable();
		this.getContentPane().add(chordTablePane, BorderLayout.CENTER);
		
		this.getContentPane().add(loadSavePanel, BorderLayout.NORTH);
		
		resetButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				bassistManager.resetBarCount();
			}
		});
		settingsPanel.add(resetButton);
		boxBassists = new JComboBox<>(SimpleBassist.list);
		boxBassists.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					bassistManager.setBassist((SimpleBassist)boxBassists.getSelectedItem());
				}
			}
		});
		settingsPanel.add(boxBassists);
		volumeSlider = new JSlider(0, 127, bassistManager.getMidiVolume());
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				bassistManager.setMidiVolume(volumeSlider.getValue());
				volumeSlider.setToolTipText(""+bassistManager.getMidiVolume());
				volumeLabel.setText(""+bassistManager.getMidiVolume());
			}
		});
		volumePane.add(volumeSlider);
		volumeLabel.setText(""+bassistManager.getMidiVolume());
		volumePane.add(new JLabel("Volume:"));
		volumePane.add(volumeLabel);
		volumePane.setBorder(BorderFactory.createEtchedBorder());
		settingsPanel.add(volumePane);
		settingsPanel.add(statusLabel);
		this.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
	}
	
	private void makeChordTable() {
		String[] chordNames = this.bassistManager.getChordTable().chordNames;
		
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
		if (observable != bassistManager)
			return;
		
		if (arg == SimpleBassistManager.TABLE_CHANGED) {
			makeChordTable();
		}
		
		for (int i = 0; i < chordNamesLabels.length; i++) {
			if (i == bassistManager.getBarCount())
				chordNamesLabels[i].setForeground(Color.RED);
			else
				chordNamesLabels[i].setForeground(Color.BLACK);
		}
	}

}
