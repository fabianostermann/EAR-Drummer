package gui;

import init.ImageLoader;
import input.InputManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InputManagerPane extends JPanel implements ItemListener {
	
	private InputManager inputManager;
	private ConsoleArea console;
	
	public InputManagerPane(InputManager inputManager, ConsoleArea console) {
		
		this.inputManager = inputManager;
		this.console = console;
		
		//this.setTitle("Midi Input Manager");
		
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
	
	protected void initGUI() {
		
		infoPane = new JPanel();
		infoPane.setLayout(new GridLayout(0,1));
		infoPane.setBorder(BorderFactory.createTitledBorder("Input Manager"));
		
			boxMidiDevice = new JComboBox<MidiDevice.Info>(inputManager.getMidiDeviceInfo());
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
			
			updateInfo();
		this.add(infoPane);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		
		
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			try {
				inputManager.setInputDevice(null);
				console.log("Reset midi input device.");
			} catch (MidiUnavailableException ex) {
				console.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		else if (e.getStateChange() == ItemEvent.SELECTED) {
			try {
				inputManager.setInputDevice((MidiDevice.Info)boxMidiDevice.getSelectedItem());
				console.log("Midi input device changed to --- "+inputManager.getInputDevice().getDeviceInfo());
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
		
		if (inputManager.getInputDevice() != null)
			labelMidiDevice.setText("Midi Device: "+inputManager.getInputDevice().getDeviceInfo());
		else
			labelMidiDevice.setText("Midi Device: unavailable");
		
		if (inputManager.getTransmitter() != null)
			labelTransmitter.setText("Transmitter: "+inputManager.getTransmitter());
		else
			labelTransmitter.setText("Transmitter: unavailable");
		
		if (inputManager.getReceiver() != null)
			labelReceiver.setText("Receiver: "+inputManager.getReceiver());
		else
			labelReceiver.setText("Receiver: unavailable");
		
		if (inputManager.isConnected()) {
			labelConnected.setText("Connected");
			labelConnected.setIcon(ImageLoader.getImageIcon("connected"));
		} else {
			labelConnected.setText("Disconnected");
			labelConnected.setIcon(ImageLoader.getImageIcon("disconnected"));
		}
	}

}
