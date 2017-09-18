package gui;

import genetic.RhythmNote;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import output.OutputManager;

@SuppressWarnings("serial")
public class OutputManagerFrame extends ManagedFrame implements ItemListener {
	
	private OutputManager outputManager;
	
	public OutputManagerFrame(OutputManager outputManager) {
		
		this.outputManager = outputManager;
		
		this.setTitle("Midi Output Manager");
		
		this.initGUI();
		this.setSize(400,250);
		this.setResizable(false);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}
	
	//Window components
	private JPanel infoPane;
		private JComboBox<MidiDevice.Info> boxMidiDevice;
		private JLabel labelMidiDevice;
		private JLabel labelTransmitter;
		private JLabel labelReceiver;
		private JLabel labelConnected;
		private JCheckBox checkBoxIntelligentDrum;
	
	private JScrollPane scrollPaneConsole;
		private ConsoleArea console;
	
	private void initGUI() {
		
		infoPane = new JPanel();
		infoPane.setLayout(new GridLayout(0,1));
		infoPane.setBorder(BorderFactory.createTitledBorder("Info"));
		
			boxMidiDevice = new JComboBox<MidiDevice.Info>(outputManager.getMidiDeviceInfo());
			boxMidiDevice.addItem(null);
			boxMidiDevice.setSelectedItem(null);
			boxMidiDevice.addItemListener(this);
			infoPane.add(boxMidiDevice);
			
			labelMidiDevice = new JLabel();
			infoPane.add(labelMidiDevice);
			labelTransmitter = new JLabel();
			infoPane.add(labelTransmitter);
			labelReceiver = new JLabel();
			infoPane.add(labelReceiver);
			labelConnected = new JLabel();
			infoPane.add(labelConnected);
			
			checkBoxIntelligentDrum = new JCheckBox("use intelligent drum");
			checkBoxIntelligentDrum.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getSource() == checkBoxIntelligentDrum) {
						RhythmNote.setIntelligentDrumEnabled(e.getStateChange() == ItemEvent.SELECTED);
					}
				}
			});
			infoPane.add(checkBoxIntelligentDrum);
			
			updateInfo();
		this.getContentPane().add(infoPane, BorderLayout.NORTH);
		
			console = new ConsoleArea();
			console.setEditable(false);
		
			console.log("Output Manager Console initialized.");
		
			scrollPaneConsole = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(scrollPaneConsole, BorderLayout.CENTER);
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			try {
				outputManager.setOutputDevice(null);
				console.log("Reset midi output device.");
			} catch (MidiUnavailableException ex) {
				console.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		else if (e.getStateChange() == ItemEvent.SELECTED) {
			try {
				outputManager.setOutputDevice((MidiDevice.Info)boxMidiDevice.getSelectedItem());
				console.log("Midi output device changed to --- "+outputManager.getOutputDevice().getDeviceInfo());
			} catch (MidiUnavailableException ex) {
				console.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		updateInfo();
	}
	
	public void updateInfo() {
		
		if (outputManager.getOutputDevice() != null)
			labelMidiDevice.setText("Midi Device: "+outputManager.getOutputDevice().getDeviceInfo());
		else
			labelMidiDevice.setText("Midi Device: unavailable");
		
		if (outputManager.getTransmitter() != null)
			labelTransmitter.setText("Transmitter: "+outputManager.getTransmitter());
		else
			labelTransmitter.setText("Transmitter: unavailable");
		
		if (outputManager.getReceiver() != null)
			labelReceiver.setText("Receiver: "+outputManager.getReceiver());
		else
			labelReceiver.setText("Receiver: unavailable");
		
		labelConnected.setText("Connected: " + outputManager.isConnected());
		
	}
	
	
}
