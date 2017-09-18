package gui;

import genetic.combine.Combi;
import genetic.combine.CombiManager;
import genetic.combine.pattern.PatternFactor;
import genetic.combine.solo.SoloFactor;
import init.ImageLoader;
import init.Streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

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
public class CombiManagerFrame extends ManagedFrame implements LoadSaveable {

	private static final int MAX_NUMBER_OF_SLOTS = 5;
	private CombiManager combiManager;
	
	public CombiManagerFrame(CombiManager combiManager) {
		
		this.combiManager = combiManager;
		
		this.setTitle("CombiManager");
		
		this.initGUI();
		this.pack();
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		startSliderColorThread();
	}

	private JPanel combiPane = new JPanel(new GridLayout(1,0));
		private ArrayList<CombiPanel> combiPanelList = new ArrayList<CombiPanel>();
		private JButton addCombiButton = ImageLoader.createButton("+");
	
	private LoadSavePanel loadSavePanel = new LoadSavePanel(this, "combis", "combi1");
		
	private void initGUI() {
		
		addCombiButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { addNewCombi(); }
		});
		updateCombis();
		
		this.getContentPane().add(combiPane, BorderLayout.CENTER);
		this.getContentPane().add(loadSavePanel, BorderLayout.NORTH);
	}
	
	private void updateCombis() {
		combiPane.removeAll();
		combiPanelList.clear();
		for (Combi combi : combiManager.getList()) {
			CombiPanel combiPanel = new CombiPanel(combi, combiManager.getPatternFactors(), combiManager.getSoloFactors());
			combiPanelList.add(combiPanel);
			combiPane.add(combiPanel);
		}
		if (combiPanelList.size() == 1) {
			combiPanelList.get(0).setRemoveButtonEnabled(false);
		}
		int numOfSlots = combiPanelList.size();
		do {
			Slot dummy = new Slot();
			if (numOfSlots == combiPanelList.size()) {
				dummy.setLayout(new BorderLayout());
				dummy.add(addCombiButton, BorderLayout.CENTER);
			}
			combiPane.add(dummy);
			numOfSlots++;
		} while (numOfSlots < MAX_NUMBER_OF_SLOTS);
		this.revalidate();
		this.pack();
	}
	
	private void addNewCombi() {
		combiManager.addEmptyCombi();
		updateCombis();
	}
	
	private void removeCombi(Combi combi) {
		if (combiPanelList.size() > 1) {
			combiManager.removeCombi(combi);
			updateCombis();
		} else
			Streams.combiOut.println("CombiManagerFrame will not remove last combiPanel");
	}
	
	private void startSliderColorThread() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				boolean running = true;
				while (running) {
					
					for (CombiPanel combiPanel : combiPanelList) {
					
						float color = combiPanel.getColor();
						if (color > 0) {
							color -= 0.02f;
							color = Math.max(0, color);
							combiPanel.setColor(color);
						}
						
						
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	private class CombiPanel extends Slot implements ChangeListener, ItemListener, Observer {
		
		private Combi combi;
		
		private JComboBox<PatternFactor> patternComboBox;
		private JComboBox<SoloFactor> soloComboBox;
		private JSlider slider;
		private JButton removeButton;
		
		private Color hueColor;
		
		public static final int SLIDER_FACTOR = 1000;
		
		private CombiPanel(final Combi combi, PatternFactor[] patternFactors, SoloFactor[] soloFactors) {
			
			this.combi = combi;
			combi.addObserver(this);
			
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
			
			removeButton = ImageLoader.createButton("");
			setRemoveButtonEnabled(true);
			removeButton.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) { removeCombi(combi); }
			});
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(soloComboBox);
			this.add(patternComboBox);
			this.add(slider);
			this.add(removeButton);
			
			this.setColor(0f);
		}

		public void setRemoveButtonEnabled(boolean enabled) {
			this.removeButton.setEnabled(enabled);
			if (enabled)
				this.removeButton.setText("-");
			else
				this.removeButton.setText(" ");
		}
		
		//0f = green, 1f = red;
		public void setColor(float hue) {
			
			float correctedHue = (1 - hue) * 0.333f;
			
			hueColor = new Color(Color.HSBtoRGB(correctedHue, 1f, 1f));
			
			if (hue != 0f) {
				slider.setBackground(hueColor);
//				this.setBackground(hueColor);
			} else {
				slider.setBackground(Color.BLACK);
//				this.setBackground(Color.BLACK);
			}
		}
		
		@Override
		public void update(Observable combi, Object weightedRate) {
			
			float rate = (float)weightedRate;
			
			if (combi == this.combi) {
				if (rate > this.getColor())
					this.setColor(rate);
			}
			
		}
		
		public float getColor() {
			Color col = hueColor;
			float hue = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null)[0];
			float color = 1 - (hue / 0.333f);
			return color;
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
		
		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			this.soloComboBox.setEnabled(enabled);
			this.patternComboBox.setEnabled(enabled);
			this.slider.setEnabled(enabled);
			setRemoveButtonEnabled(enabled);
		}
	}
	
	private class Slot extends JPanel {
		
		private Slot() {
			this.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE));
		}
	}

	@Override
	public void loadFromFile(RandomAccessFile raf) throws IOException {
		String line = "";
		ArrayList<Combi> combis = new ArrayList<>();
		while ((line = raf.readLine()) != null) {
			String[] combiSpec = line.split(":");
			if (combiSpec.length == 3) {
				SoloFactor soloFactor = combiManager.getSoloFactor(combiSpec[0]);
				PatternFactor patternFactor = combiManager.getPatternFactor(combiSpec[1]);
				Combi combi = new Combi(patternFactor, soloFactor);
				try { combi.setWeight(Float.parseFloat(combiSpec[2])); }
				catch (NumberFormatException e)
				{ System.err.println("Bad float ("+combiSpec[2]+") in line " + line + " while parsing "+raf.getFD()); }
				combis.add(combi);
			}
		}
		this.combiManager.getList().clear();
		this.combiManager.getList().addAll(combis);
		this.updateCombis();
	}

	@Override
	public void saveToFile(RandomAccessFile raf) throws IOException {
		for (CombiPanel combiPanel : this.combiPanelList) {
			raf.writeBytes(combiPanel.combi.soloFactor.getName()+":"
					+ combiPanel.combi.patternFactor.getName()+":"
					+ combiPanel.combi.getWeight()+"\n");
		}
	}
}