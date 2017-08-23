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
		private JButton buttonRec = new JButton("REC");
		private JButton buttonStop = new JButton("STOP");
		private JButton buttonPlay = new JButton("PLAY");
	
	private void initGUI() {

		buttonPane.setLayout(new FlowLayout());
		
			buttonRec.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {
					buttonRecClicked();
				}
			});
			buttonPane.add(buttonRec);
			
			buttonStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {
					buttonStopClicked();
				}
			});
			buttonPane.add(buttonStop);
			buttonStop.setEnabled(false);
			
			buttonPlay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent a) {
					buttonPlayClicked();
				}
			});
			buttonPane.add(buttonPlay);
			
			// TODO manage enabling of play button
			buttonPlay.setEnabled(true);
		
		this.getContentPane().add(buttonPane, BorderLayout.CENTER);
	}
	
	private void buttonStopClicked() {
		this.soloRecorder.stopRecording();
		buttonRec.setEnabled(true);
		buttonStop.setEnabled(false);
	}
	
	private void buttonRecClicked() {
		this.soloRecorder.startNewRecording();
		buttonRec.setEnabled(false);
		buttonStop.setEnabled(true);
	}	
	
	private void buttonPlayClicked() {
		this.soloRecorder.playbackRecord();
	}
}
