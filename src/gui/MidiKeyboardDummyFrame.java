package gui;

import input.InputManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.sound.midi.ShortMessage;
import javax.swing.JButton;

import output.OutputManager;

@SuppressWarnings("serial")
public class MidiKeyboardDummyFrame extends ManagedFrame implements KeyListener {
	
	private OutputManager outputManager;
	private InputManager inputManager;
	
	private static int CHANNEL = 0;
	private static int VOLUME = 100;
	private static int VOLUME_CHANGE = 25;
	
	private HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
	
	public MidiKeyboardDummyFrame(OutputManager outputManager, InputManager inputManager) {
		
		if (outputManager == null || inputManager == null)
			throw new NullPointerException();
			
		this.inputManager = inputManager;
		this.outputManager = outputManager;
		
		this.setTitle("Midi Keyboard Dummy");
		
		this.initGUI();
		this.pack();
		
		useDummyButton.addKeyListener(this);

		this.setLocationByPlatform(true);
		this.setVisible(true);

	}
	
	//Window components
	private JButton useDummyButton;
	
	private void initGUI() {
		
		useDummyButton = new JButton("USE DUMMY");
		useDummyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startC7sharp9Thread();
			}
		});
		this.getContentPane().add(useDummyButton);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyDown(e.getKeyCode(), true);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyDown(e.getKeyCode(), false);
	}
	
	private void keyDown(int keyCode, boolean down) {
		if (down) {
			if (keyMap.get(new Integer(keyCode)) == null || (!(keyMap.get(new Integer(keyCode))))) {
				keyMap.put(keyCode, true);
				
				if (keyCode == KeyEvent.VK_UP)
					VOLUME += VOLUME_CHANGE;
				else if (keyCode == KeyEvent.VK_DOWN)
					VOLUME -= VOLUME_CHANGE;
				else
					play(getMidi(keyCode), true);
			}
		} else {
			keyMap.put(keyCode, false);
			
			if (keyCode == KeyEvent.VK_UP)
				VOLUME -= VOLUME_CHANGE;
			else if (keyCode == KeyEvent.VK_DOWN)
				VOLUME += VOLUME_CHANGE;
			else
				play(getMidi(keyCode), false);
		}
	}
	
	
	private int getMidi(int keyCode) {
		
		switch (keyCode) {
		case KeyEvent.VK_1: return 60; //C4
		case KeyEvent.VK_2:	return 63;
		case KeyEvent.VK_3:	return 65;
		case KeyEvent.VK_4:	return 67;
		case KeyEvent.VK_5: return 70;
		case KeyEvent.VK_6:	return 72;
		case KeyEvent.VK_7:	return 75;
		case KeyEvent.VK_8: return 77;
		case KeyEvent.VK_9: return 79;
		case KeyEvent.VK_0: return 82;
		
		case KeyEvent.VK_Q: return 59;
		case KeyEvent.VK_W:	return 62;
		case KeyEvent.VK_E:	return 64;
		case KeyEvent.VK_R:	return 66;
		case KeyEvent.VK_T: return 69;
		case KeyEvent.VK_Z:	return 71;
		case KeyEvent.VK_U:	return 74;
		case KeyEvent.VK_I: return 76;
		case KeyEvent.VK_O: return 78;
		case KeyEvent.VK_P: return 81;
		
		case KeyEvent.VK_A: return 58;
		case KeyEvent.VK_S:	return 61;
		case KeyEvent.VK_D:	return 63;
		case KeyEvent.VK_F:	return 65;
		case KeyEvent.VK_G: return 68;
		case KeyEvent.VK_H:	return 70;
		case KeyEvent.VK_J:	return 73;
		case KeyEvent.VK_K: return 75;
		case KeyEvent.VK_L: return 77;
		
		case KeyEvent.VK_Y: return 57;
		case KeyEvent.VK_X:	return 60;
		case KeyEvent.VK_C:	return 62;
		case KeyEvent.VK_V:	return 64;
		case KeyEvent.VK_B: return 67;
		case KeyEvent.VK_N:	return 69;
		case KeyEvent.VK_M:	return 72;
		
		default: return -1;
		}
		
	}

	public void play(int midi, boolean noteOn) {
		
		if (midi < 0 || midi > 127)
			return;
		
		int midiCommand;
		if (noteOn)
			midiCommand = ShortMessage.NOTE_ON;
		else
			midiCommand = ShortMessage.NOTE_OFF;
		
		try {
			inputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
			outputManager.getReceiver().send(new ShortMessage(midiCommand, CHANNEL, midi, VOLUME), -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startC7sharp9Thread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				play(24, true);
//				play(64, true);
//				play(70, true);
//				play(75, true);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				play(24, false);
//				play(64, false);
//				play(70, false);
//				play(75, false);
			}
		}).start();
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
