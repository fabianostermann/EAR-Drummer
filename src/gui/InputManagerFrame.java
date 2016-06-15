package gui;

import input.InputManager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class InputManagerFrame extends ManagedFrame implements ItemListener {
	
	private InputManager inputManager;
	
	public InputManagerFrame(InputManager inputManager) {
		
		this.inputManager = inputManager;
		
		this.setTitle("Midi Input Manager");
		
		this.initGUI();
		this.setSize(400,300);
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
	
	private JScrollPane scrollPaneConsole;
		private ConsoleArea console;
	
	protected void initGUI() {
		
		infoPane = new JPanel();
		infoPane.setLayout(new GridLayout(0,1));
		infoPane.setBorder(BorderFactory.createTitledBorder("Info"));
		
			boxMidiDevice = new JComboBox<MidiDevice.Info>(inputManager.getMidiDeviceInfo());
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
			
			
			updateInfo();
		this.getContentPane().add(infoPane, BorderLayout.NORTH);
		
			console = new ConsoleArea();
			console.setEditable(false);
			
			console.log("Input Manager Console initialized.");
			
			scrollPaneConsole = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(scrollPaneConsole, BorderLayout.CENTER);
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
		
		labelConnected.setText("Connected: " + inputManager.isConnected());
		
	}

		
}
