package gui;

import genetic.RhythmNote;
import init.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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

import output.OutputManager;

@SuppressWarnings("serial")
public class OutputManagerPane extends JPanel implements ItemListener {
	
	private OutputManager outputManager;
	private ConsoleArea console;
	
	public OutputManagerPane(OutputManager outputManager, ConsoleArea console) {
		
		this.outputManager = outputManager;
		this.console = console;
		
		//this.setTitle("Midi Output Manager");
		
		this.initGUI();
		//this.setSize(400,250);
		//this.setResizable(false);

		//this.setLocationByPlatform(true);
		//this.setVisible(false);
		
	}
	
	//Window components
	private JPanel infoPane;
		private JComboBox<MidiDevice.Info> boxMidiDevice;
		private JLabel labelMidiDevice;
		private JLabel labelTransmitter;
		private JLabel labelReceiver;
		private JLabel labelConnected;
		private JCheckBox checkBoxIntelligentDrum;
	
	private void initGUI() {
		
		infoPane = new JPanel();
		infoPane.setLayout(new GridLayout(0,1));
		infoPane.setBorder(BorderFactory.createTitledBorder("Output Manager"));
		
			boxMidiDevice = new JComboBox<MidiDevice.Info>(outputManager.getMidiDeviceInfo());
			boxMidiDevice.addItem(null);
			boxMidiDevice.setSelectedItem(null);
			boxMidiDevice.addItemListener(this);
			infoPane.add(boxMidiDevice);
			
			labelMidiDevice = new JLabel();
			infoPane.add(labelMidiDevice);
			labelConnected = new JLabel();
			infoPane.add(labelConnected);
			labelTransmitter = new JLabel();
			infoPane.add(labelTransmitter);
			labelReceiver = new JLabel();
			infoPane.add(labelReceiver);
			
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
		this.add(infoPane, BorderLayout.NORTH);
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
	
	@Override
	public void setPreferredSize(Dimension dim) {
		for (Component component : infoPane.getComponents()) {
			component.setPreferredSize(new Dimension(dim.width-20, 25));
		}
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
		
		if (outputManager.isConnected()) {
			labelConnected.setText("Connected");
			labelConnected.setIcon(ImageLoader.getImageIcon("connected"));
		} else {
			labelConnected.setText("Disconnected");
			labelConnected.setIcon(ImageLoader.getImageIcon("disconnected"));
		}
	}
}
