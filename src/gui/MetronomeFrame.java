package gui;

import init.Settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import playback.Metronome;
import playback.MetronomeListener;

@SuppressWarnings("serial")
public class MetronomeFrame extends ManagedFrame implements MetronomeListener, ChangeListener{
	
	private Metronome metronome;

	public MetronomeFrame(Metronome metronome) {
		
		this.metronome = metronome;
		
		this.setTitle("Metronome");
		
		this.initGUI();
		this.pack();
		this.setResizable(false);
		
		this.metronome.addMetronomeListener(this);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
	}

	//Window components
	private JPanel buttonPane = new JPanel();
		private JButton buttonStart = new JButton("START");
		private JButton buttonStop = new JButton("STOP");
	
	private JPanel sliderPane = new JPanel(new GridLayout(0, 1, 10, 10));
		private JSlider sliderTpm;
		private JSlider sliderShuffle;
		
	private JPanel lightPane = new JPanel(new GridLayout(1,0));
		private JTextField[] lights;
	
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
		
		
			sliderTpm = new JSlider(Settings.MIN_TPM, Settings.MAX_TPM, this.metronome.getTpm());
			sliderTpm.setMajorTickSpacing(100);
			sliderTpm.setMinorTickSpacing(10);
			sliderTpm.setPaintTicks(true);
			sliderTpm.setPaintLabels(true);
			sliderTpm.addChangeListener(this);
			sliderTpm.setToolTipText("Ticks per minute: "+sliderTpm.getValue());
			sliderTpm.setBorder(BorderFactory.createTitledBorder("Ticks per minute"));
			sliderPane.add(sliderTpm);
			
			sliderShuffle = new JSlider(50, 100, (int)(this.metronome.getShuffle()*100));
			sliderShuffle.setMajorTickSpacing(17);
			sliderShuffle.setPaintTicks(true);
			sliderShuffle.setPaintLabels(true);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put( new Integer( 50 ), new JLabel("0.5") );
			labelTable.put( new Integer( 67 ), new JLabel("0.67") );
			labelTable.put( new Integer( 100 ), new JLabel("1.0") );
			sliderShuffle.setLabelTable( labelTable );
			sliderShuffle.addChangeListener(this);
			sliderShuffle.setToolTipText("Shuffle factor: "+(float)sliderShuffle.getValue()/100);
			sliderShuffle.setBorder(BorderFactory.createTitledBorder("Shuffle factor"));
			sliderPane.add(sliderShuffle);
		
			sliderPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.getContentPane().add(sliderPane, BorderLayout.NORTH);
			
		
		lights = new JTextField[metronome.getTicks()];
			for (int i = 0; i < lights.length; i++) {
				JTextField light = new JTextField(""+i);
				light.setEditable(false);
				light.setBackground(Color.BLACK);
				lightPane.add(light);
				
				lights[i] = light;
			}
			
		this.getContentPane().add(lightPane, BorderLayout.SOUTH);
			
	}

	@Override
	public void tick(Metronome metronome) {
		
		int tick = metronome.getTick();
		
		for (int i = 0; i < lights.length; i++) {
			if (i == tick)
				lights[i].setBackground(Color.RED);
			else
				lights[i].setBackground(Color.BLACK);
		}
	}
	
	private void buttonStopClicked() {
		
		this.metronome.stop();
		
		this.buttonStop.setEnabled(false);
		this.buttonStart.setEnabled(true);
		
		update();
	}
	
	private void buttonStartClicked() {

		this.metronome.start();
		
		this.buttonStart.setEnabled(false);
		this.buttonStop.setEnabled(true);
		
		update();
	}


	@Override
	public void stateChanged(ChangeEvent ce) {
		
		if (ce.getSource() == sliderTpm) {
			this.metronome.setTpm(sliderTpm.getValue());
		}
		
		if (ce.getSource() == sliderShuffle) {
			this.metronome.setShuffle((float)sliderShuffle.getValue()/100);
		}
		
		update();
		
	}
	
	public void update() {
		sliderTpm.setValue(this.metronome.getTpm());
		sliderTpm.setToolTipText("Ticks per minute: "+sliderTpm.getValue());
		sliderShuffle.setValue((int)(this.metronome.getShuffle()*100));
		sliderShuffle.setToolTipText("Shuffle: "+(float)sliderShuffle.getValue()/100);
	}

	
	
}
