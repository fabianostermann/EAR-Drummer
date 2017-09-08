package gui;

import genetic.Evolution;
import genetic.ProgressObservable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class EvolutionFrame extends ManagedFrame implements Observer, ChangeListener {
	
	private Evolution evolution;

	public EvolutionFrame(Evolution evolution) {
		
		this.evolution = evolution;
		
		this.setTitle("Evolution Controller");
		
		this.initGUI();
		this.pack();
//		this.setResizable(false);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		evolution.addObserver(this);
	}

	//Window components
	private JPanel buttonPane = new JPanel(new BorderLayout());
		private JButton buttonStart = new JButton("START");
		private JButton buttonPause = new JButton("PAUSE");
		private JButton buttonResume = new JButton("RESUME");
		private JButton buttonStop = new JButton("STOP");
		
	private JScrollPane patternScrollPane;
		private JTextArea patternArea = new JTextArea(0,10);
		
	private JPanel infoPane = new JPanel(new GridLayout(0,2,10,10));
		private JSpinner populationSizeSpinner;
		private JSpinner sleepTimeSpinner;
		private JSpinner mutationExpanisionLimitSpinner;
		private JSpinner inputWindowSizeSpinner;
		
	private JProgressBar progressBar = new JProgressBar();


	
	private void initGUI() {
		
		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				buttonStartClicked();
			}
		});	
		buttonPane.add(buttonStart, BorderLayout.NORTH);
		
		buttonPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				buttonPauseClicked();
			}
		});	
		buttonPane.add(buttonPause, BorderLayout.WEST);
		
		buttonResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				buttonResumeClicked();
			}
		});	
		buttonPane.add(buttonResume, BorderLayout.EAST);
		
		buttonStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				buttonStopClicked();
			}
		});	
		buttonPane.add(buttonStop, BorderLayout.SOUTH);
		
		this.getContentPane().add(buttonPane, BorderLayout.EAST);
		
		infoPane.add(new JLabel("Population size:"));
		populationSizeSpinner = new JSpinner(new SpinnerNumberModel(this.evolution.POPULATION_SIZE, 1, Integer.MAX_VALUE, 1));
		populationSizeSpinner.addChangeListener(this);
		infoPane.add(populationSizeSpinner);
		
		infoPane.add(new JLabel("Sleep time (ms):"));
		sleepTimeSpinner = new JSpinner(new SpinnerNumberModel(this.evolution.SLEEP_TIME, 0, Integer.MAX_VALUE, 1));
		sleepTimeSpinner.addChangeListener(this);
		infoPane.add(sleepTimeSpinner);
		
		infoPane.add(new JLabel("Mutation expansion limit:"));
		mutationExpanisionLimitSpinner = new JSpinner(new SpinnerNumberModel(this.evolution.MUTATION_EXPANSION_LIMIT, 0, Integer.MAX_VALUE, 1));
		mutationExpanisionLimitSpinner.addChangeListener(this);
		infoPane.add(mutationExpanisionLimitSpinner);
		
		infoPane.add(new JLabel("Input window size (ms):"));
		inputWindowSizeSpinner = new JSpinner(new SpinnerNumberModel(this.evolution.INPUT_WINDOW_SIZE, 0, Integer.MAX_VALUE, 1));
		inputWindowSizeSpinner.addChangeListener(this);
		infoPane.add(inputWindowSizeSpinner);
		
		this.getContentPane().add(infoPane, BorderLayout.WEST);
		
		this.getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		patternArea.setEditable(false);
		patternScrollPane = new JScrollPane(patternArea);
		this.getContentPane().add(patternScrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {
		
		if (event.getSource() == populationSizeSpinner) {
			this.evolution.POPULATION_SIZE = ((SpinnerNumberModel)populationSizeSpinner.getModel()).getNumber().intValue();
			((SpinnerNumberModel)populationSizeSpinner.getModel()).setValue(this.evolution.POPULATION_SIZE);
		} else
			if (event.getSource() == sleepTimeSpinner) {
				this.evolution.SLEEP_TIME = ((SpinnerNumberModel)sleepTimeSpinner.getModel()).getNumber().intValue();
				((SpinnerNumberModel)sleepTimeSpinner.getModel()).setValue(this.evolution.SLEEP_TIME);
			} else
				if (event.getSource() == mutationExpanisionLimitSpinner) {
					this.evolution.MUTATION_EXPANSION_LIMIT = ((SpinnerNumberModel)mutationExpanisionLimitSpinner.getModel()).getNumber().intValue();
					((SpinnerNumberModel)mutationExpanisionLimitSpinner.getModel()).setValue(this.evolution.MUTATION_EXPANSION_LIMIT);
				} else
					if (event.getSource() == inputWindowSizeSpinner) {
						this.evolution.INPUT_WINDOW_SIZE = ((SpinnerNumberModel)inputWindowSizeSpinner.getModel()).getNumber().intValue();
						((SpinnerNumberModel)inputWindowSizeSpinner.getModel()).setValue(this.evolution.INPUT_WINDOW_SIZE);
					}
	}
	
	private void buttonStartClicked() {
		evolution.startEvolution();
		
		buttonStart.setEnabled(false);
		buttonStop.setEnabled(true);
		buttonPause.setEnabled(true);
		buttonResume.setEnabled(false);
	}
	
	private void buttonStopClicked() {
		evolution.stopEvolution();
		
		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		buttonPause.setEnabled(false);
		buttonResume.setEnabled(false);
	}

	private void buttonResumeClicked() {
		evolution.resumeEvolution();
		
		buttonResume.setEnabled(false);
		buttonPause.setEnabled(true);
	}

	private void buttonPauseClicked() {
		evolution.pauseEvolution();
		
		buttonResume.setEnabled(true);
		buttonPause.setEnabled(false);
	}



	@Override
	public void update(Observable o, Object arg) {
		
		if (o == this.evolution) {
			
			if (arg == ProgressObservable.PROGRESS_CHANGED) {
				this.progressBar.setValue(this.evolution.getProgressValue());
				
				if (this.evolution.getProgressString() != null) {
					this.progressBar.setStringPainted(true);
					this.progressBar.setString(this.evolution.getProgressString());
				} else {
					this.progressBar.setStringPainted(false);
					this.progressBar.setString(null);
				}
			}
			
			if (arg == ProgressObservable.STRINGLIST_CHANGED) {
				this.setStringList(this.evolution.getStringList());
			}
			
			if (arg == ProgressObservable.ALARM_CHANGED) {
				if (this.evolution.isAlarm()) {
					this.progressBar.setBackground(Color.ORANGE);
					this.progressBar.setForeground(Color.RED);
				} else {
					this.progressBar.setBackground(Color.GRAY);
					this.progressBar.setForeground(Color.DARK_GRAY);
				}
			}
		}
	}

	private void setStringList(ArrayList<String> stringList) {
		
		String text = "";
		
		if (stringList != null)
			for (String s : stringList) {
				text += s + "\n";
			}
		
		this.patternArea.setText(text);
	}

	
	
}
