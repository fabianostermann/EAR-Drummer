package output;

import init.Settings;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class OutputManager {

	private MidiDevice outputDevice;
	private Transmitter transmitter;
	private Receiver receiver;
	
	public OutputManager() {}
	
	public OutputManager(MidiDevice outputDevice) throws MidiUnavailableException {
		this(outputDevice, null);
	}
	
	public OutputManager(Transmitter transmitter) throws MidiUnavailableException {
		this(null, transmitter);
	}
	
	public OutputManager(MidiDevice outputDevice, Transmitter transmitter) throws MidiUnavailableException {
		
		this.outputDevice = outputDevice;
		if (outputDevice != null)
			this.receiver = outputDevice.getReceiver();
		this.transmitter = transmitter;
		
		if (outputDevice == null)
			try {
				setOutputDevice(Settings.getDefaultOutputDevice());
			}
			catch (Exception e) {
				setOutputDevice(null);
			}
	}
	
	public MidiDevice.Info[] getMidiDeviceInfo() {
		return MidiSystem.getMidiDeviceInfo();
	}
	
	public void setOutputDevice(MidiDevice.Info info) throws MidiUnavailableException {
		
		if (info == null) {
			this.transmitter.setReceiver(null);
			this.outputDevice = null;
			this.receiver = null;
			return;
		}
		
		MidiDevice newOutputDevice = MidiSystem.getMidiDevice(info);
		newOutputDevice.open();
		Receiver newReceiver = newOutputDevice.getReceiver();
		
		if (this.receiver != null)
			this.receiver.close();
		
		if (this.outputDevice != null)
			this.outputDevice.close();
		
		if (this.transmitter != null)
			this.transmitter.setReceiver(newReceiver);
		
		this.outputDevice = newOutputDevice;
		this.receiver = newReceiver;
	}
	
	public void setTransmitter(Transmitter transmitter) {
		
		if (this.receiver != null && transmitter != null)
			transmitter.setReceiver(this.receiver);
		
		if (this.transmitter != null)
		this.transmitter.close();
		
		this.transmitter = transmitter;
	}
	
	public boolean isConnected() {
		return transmitter != null && receiver != null && transmitter.getReceiver() == receiver;
	}
	
	public MidiDevice getOutputDevice() {
		return this.outputDevice;
	}
	
	public Transmitter getTransmitter() {
		return this.transmitter;
	}
	
	public Receiver getReceiver() {
		return this.receiver;
	}
	
}
