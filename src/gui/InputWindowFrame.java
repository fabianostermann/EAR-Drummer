package gui;

import input.InputWindow;


@SuppressWarnings("serial")
public class InputWindowFrame extends ManagedFrame {
	
	private InputWindow inputWindow;
	
	public InputWindowFrame(InputWindow inputWindow) {
		
		this.inputWindow = inputWindow;
		
		this.setTitle("Input Window");
		
		this.initGUI();
		this.setSize(1000, 250);
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}
	
	//Window components
	private InputWindowPane inputWindowPane;
	
	private void initGUI() {
		
		inputWindowPane = new InputWindowPane(this.inputWindow);
		this.getContentPane().add(inputWindowPane);
		
	}
	
	public void setInputWindow(InputWindow inputWindow) {
		this.inputWindow = inputWindow;
	}

	
	
}

