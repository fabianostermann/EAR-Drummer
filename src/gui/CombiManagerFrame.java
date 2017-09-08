package gui;

import genetic.combine.Combi;
import genetic.combine.CombiManager;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.solo.SoloFactor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class CombiManagerFrame extends ManagedFrame {

	private CombiManager combiManager;
	
	public CombiManagerFrame(CombiManager combiManager) {
		
		this.combiManager = combiManager;
		
		this.setTitle("CombiManager");
		
		this.initGUI();
		this.pack();
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		// TODO startSliderColorThread();
	}

	private JPanel combiPane = new JPanel(new GridLayout(1,0));
		private ArrayList<CombiPanel> combiPanelList = new ArrayList<CombiPanel>();
		
	private void initGUI() {
		
		for (Combi combi : combiManager.getList()) {
			CombiPanel combiPanel = new CombiPanel(combi, combiManager.getPatternFactors(), combiManager.getSoloFactors());
			combiPanelList.add(combiPanel);
			combiPane.add(combiPanel);
		}
		
		this.getContentPane().add(combiPane, BorderLayout.CENTER);
	}
		
	private class CombiPanel extends JPanel {
		
		private Combi combi;
		
		private JComboBox<PatternFactor> patternComboBox;
		private JComboBox<SoloFactor> soloComboBox;
		private JSlider slider;
		
		private CombiPanel(Combi combi, PatternFactor[] patternFactors, SoloFactor[] soloFactors) {
			
			this.combi = combi;

			patternComboBox = new JComboBox<>(patternFactors);
			soloComboBox = new JComboBox<>(soloFactors);
			
			slider = new JSlider(0, 100, 50);
			slider.setOrientation(JSlider.VERTICAL);
			
			this.setLayout(new GridLayout(0,1));
			this.add(soloComboBox);
			this.add(patternComboBox);
			this.add(slider);
		}
		
	}
}
