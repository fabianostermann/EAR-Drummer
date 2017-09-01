package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import record.SoloRecorder;

/* TODO include this in metronome frame
 * and forbid changing metronome settings while recording */

@SuppressWarnings("serial")
public class SoloRecorderFrame extends ManagedFrame {
	
	private SoloRecorder soloRecorder;

	public SoloRecorderFrame(SoloRecorder soloRecorder) {
		
		this.soloRecorder = soloRecorder;
		loadSavePanel = new LoadSavePanel(soloRecorder, "records");
		
		this.setTitle("Solo Recorder");
		
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
		
	private LoadSavePanel loadSavePanel;
	
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
		
		this.getContentPane().add(buttonPane, BorderLayout.CENTER);
		
		this.getContentPane().add(loadSavePanel, BorderLayout.NORTH);
	}
	
	private void buttonStopClicked() {
		this.soloRecorder.stopRecording();
		this.soloRecorder.stopPlayback();
		loadSavePanel.setEnabled(true);
		buttonRec.setEnabled(true);
		buttonStop.setEnabled(false);
		buttonPlay.setEnabled(true);
	}
	
	private void buttonRecClicked() {
		this.soloRecorder.stopPlayback();
		this.soloRecorder.startNewRecording();
		loadSavePanel.setEnabled(false);
		buttonRec.setEnabled(false);
		buttonStop.setEnabled(true);
		buttonPlay.setEnabled(false);
	}	
	
	private void buttonPlayClicked() {
		this.soloRecorder.stopRecording();
		this.soloRecorder.playbackRecord();
		loadSavePanel.setEnabled(true);
		buttonRec.setEnabled(false);
		buttonStop.setEnabled(true);
		buttonPlay.setEnabled(false);
	}
}
