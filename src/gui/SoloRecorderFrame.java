package gui;

import init.ImageLoader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import record.SoloRecorder;

/* TODO maybe include this in metronome frame */

@SuppressWarnings("serial")
public class SoloRecorderFrame extends ManagedFrame {
	
	private SoloRecorder soloRecorder;

	public SoloRecorderFrame(SoloRecorder soloRecorder) {
		
		this.soloRecorder = soloRecorder;
		loadSavePanel = new LoadSavePanel(soloRecorder, "records", "record1");
		
		this.setTitle("Solo Recorder");
		
		this.initGUI();
//		this.setSize(250, 220);
		this.setResizable(true);
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(false);
		
	}

	//Window components
	private JPanel buttonPane = new JPanel();
		private JButton buttonRec = ImageLoader.createButton("REC");
		private JButton buttonStop = ImageLoader.createButton("STOP");
		private JButton buttonPlay = ImageLoader.createButton("PLAY");
		private JCheckBox latencyCheckBox = new JCheckBox("Latency:");
		private JSpinner latencySpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		
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
			
			latencyCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					latencySpinner.setEnabled(latencyCheckBox.isSelected());
			}});
			buttonPane.add(latencyCheckBox);
			latencyCheckBox.setSelected(false);
			buttonPane.add(latencySpinner);
			latencySpinner.setEnabled(false);
		
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
		if (latencyCheckBox.isSelected())
			this.soloRecorder.correctLatency(((SpinnerNumberModel)latencySpinner.getModel()).getNumber().longValue());
		else
			this.soloRecorder.correctLatency(0L);
		this.soloRecorder.playbackRecord();
		loadSavePanel.setEnabled(false);
		buttonRec.setEnabled(false);
		buttonStop.setEnabled(true);
		buttonPlay.setEnabled(false);
	}
}
