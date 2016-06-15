package input;

import init.Settings;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class InputManager {

	private MidiDevice inputDevice;
	private Transmitter transmitter;
	private Receiver receiver;
	
	public InputManager() {}
	
	public InputManager(MidiDevice inputDevice) throws MidiUnavailableException {
		this(inputDevice, null);
	}
	
	public InputManager(Receiver receiver) throws MidiUnavailableException {
		this(null, receiver);
	}
	
	public InputManager(MidiDevice inputDevice, Receiver receiver) throws MidiUnavailableException {
		
		this.inputDevice = inputDevice;
		if (inputDevice != null)
			this.transmitter = inputDevice.getTransmitter();
		this.setReceiver(receiver);
		
		if (inputDevice == null)
			try {
				setInputDevice(Settings.getDefaultInputDevice());
			}
			catch (Exception e) {
				setInputDevice(null);
			}
	}
	
	public MidiDevice.Info[] getMidiDeviceInfo() {
		return MidiSystem.getMidiDeviceInfo();
	}
	
	public void setInputDevice(MidiDevice.Info info) throws MidiUnavailableException {
		
		if (info == null) {
			if (transmitter != null)
				this.transmitter.setReceiver(null);
			this.inputDevice = null;
			this.transmitter = null;
			return;
		}
		
		MidiDevice newInputDevice = MidiSystem.getMidiDevice(info);
		newInputDevice.open();
		Transmitter newTransmitter = newInputDevice.getTransmitter();
		
		if (this.transmitter != null)
			this.transmitter.close();
		
		if (this.inputDevice != null)
			this.inputDevice.close();
		
		if (this.receiver != null)
			newTransmitter.setReceiver(this.receiver);
		
		this.inputDevice = newInputDevice;
		this.transmitter = newTransmitter;
	}
	
	public void setReceiver(Receiver receiver) {
		
		if (this.transmitter != null)
			this.transmitter.setReceiver(receiver);
		
		if (this.receiver != null)
		this.receiver.close();
		
		this.receiver = receiver;
	}
	
	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
		transmitter.setReceiver(this.receiver);
	}
	
	public boolean isConnected() {
		return transmitter != null && receiver != null && transmitter.getReceiver() == receiver;
	}
	
	public MidiDevice getInputDevice() {
		return this.inputDevice;
	}
	
	public Transmitter getTransmitter() {
		return this.transmitter;
	}
	
	public Receiver getReceiver() {
		return this.receiver;
	}
	
}
