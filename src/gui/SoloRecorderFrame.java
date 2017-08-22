package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import record.SoloRecorder;

@SuppressWarnings("serial")
public class SoloRecorderFrame extends ManagedFrame{
	
	private SoloRecorder soloRecorder;

	public SoloRecorderFrame(SoloRecorder soloRecorder) {
		
		this.soloRecorder = soloRecorder;
		
		this.setTitle("SoloRecorder");
		
		this.initGUI();
//		this.setSize(250, 220);
		this.setResizable(true);
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}

	//Window components
	private JPanel buttonPane = new JPanel();
		private JButton buttonStart = new JButton("START");
		private JButton buttonStop = new JButton("STOP");
	
	private void initGUI() {

		buttonPane.setLayout(new FlowLayout());
		
			buttonStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {
					buttonStartClicked();
				}
			});
			buttonPane.add(buttonStart);
			
			buttonStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {
					buttonStopClicked();
				}
			});
			buttonPane.add(buttonStop);
			buttonStop.setEnabled(false);
		
		this.getContentPane().add(buttonPane, BorderLayout.CENTER);	
	}
	
	private void buttonStopClicked() {
		
		this.soloRecorder.stopRecording();
	}
	
	private void buttonStartClicked() {

		this.soloRecorder.startNewRecording();
	}
}
