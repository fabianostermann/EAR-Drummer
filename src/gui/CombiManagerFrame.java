package gui;

import genetic.combine.Combi;
import genetic.combine.CombiManager;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.solo.SoloFactor;

import init.Streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		private JButton addCombiButton = new JButton("+");
		
	private void initGUI() {
		
		addCombiButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { addNewCombi(); }
		});
		updateCombis();
		
		this.getContentPane().add(combiPane, BorderLayout.CENTER);
	}
	
	private void updateCombis() {
		combiPane.removeAll();
		combiPanelList.clear();
		for (Combi combi : combiManager.getList()) {
			CombiPanel combiPanel = new CombiPanel(combi, combiManager.getPatternFactors(), combiManager.getSoloFactors());
			combiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			combiPanelList.add(combiPanel);
			combiPane.add(combiPanel);
		}
		combiPane.add(addCombiButton);
		this.revalidate();
		this.pack();
	}
	
	private void addNewCombi() {
		combiManager.addEmptyCombi();
		updateCombis();
	}
	
	private void removeCombi(Combi combi) {
		combiManager.removeCombi(combi);
		updateCombis();
	}
	
	private class CombiPanel extends JPanel implements ChangeListener, ItemListener {
		
		private Combi combi;
		
		private JComboBox<PatternFactor> patternComboBox;
		private JComboBox<SoloFactor> soloComboBox;
		private JSlider slider;
		private JButton button;
		
		public static final int SLIDER_FACTOR = 1000;
		
		private CombiPanel(final Combi combi, PatternFactor[] patternFactors, SoloFactor[] soloFactors) {
			
			this.combi = combi;

			patternComboBox = new JComboBox<>(patternFactors);
			patternComboBox.getModel().setSelectedItem(combi.patternFactor);
			patternComboBox.addItemListener(this);

			soloComboBox = new JComboBox<>(soloFactors);
			soloComboBox.getModel().setSelectedItem(combi.soloFactor);
			soloComboBox.addItemListener(this);
			
			slider = new JSlider(0, SLIDER_FACTOR, (int)(combi.getWeight()*SLIDER_FACTOR));
			slider.setOrientation(JSlider.VERTICAL);
			slider.setMajorTickSpacing(SLIDER_FACTOR / 2);
			slider.setMinorTickSpacing(SLIDER_FACTOR / 10);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put( new Integer( SLIDER_FACTOR ), new JLabel("1.0") );
			labelTable.put( new Integer( 0 ), new JLabel("0") );
			slider.setLabelTable( labelTable );
			slider.addChangeListener(this);
			
			button = new JButton("-");
			button.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) { removeCombi(combi); }
			});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(soloComboBox);
			this.add(patternComboBox);
			this.add(slider);
			this.add(button);
			
			this.setBackground(Color.RED);
			this.slider.setBackground(Color.RED);
		}
		
		@Override
		public void paint(Graphics g) {
			/* TODO draw heat or height that indicates the average fitness */
			super.paint(g);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == this.patternComboBox) {
				this.combi.patternFactor = (PatternFactor) this.patternComboBox.getModel().getSelectedItem();
			}
			else if (e.getSource() == this.soloComboBox) {
				this.combi.soloFactor = (SoloFactor) this.soloComboBox.getModel().getSelectedItem();
			}
			Streams.combiOut.println("Factors changed by comboBox: " + this.combi);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == this.slider) {
				combi.setWeight((float)this.slider.getValue() / SLIDER_FACTOR);
			}
			Streams.combiOut.println("Weight changed by slider: " + this.combi + "=" + this.combi.getWeight());
		}
	}
}
