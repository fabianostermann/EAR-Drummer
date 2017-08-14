package gui;

import genetic.Random;
import genetic.rules.Rule;
import genetic.rules.RuleManager;
import init.Settings;
import init.Streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RuleManagerFrame extends ManagedFrame {

	private static final long serialVersionUID = 6691172634839759228L;
	private static final String SAVE_FOLDER = "./weightings/";
	
	private boolean running = false;
	
	private RuleManager ruleManager;
	
	public RuleManagerFrame(RuleManager ruleManager) {
		
		this.ruleManager = ruleManager;
		
		this.setTitle("Rule Manager");
		
		this.initGUI();
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		startSliderColorThread();
	}

	//Window components
	private JPanel rulesPane = new JPanel(new GridLayout(1,0));
		private ArrayList<RuleSlider> sliderList = new ArrayList<RuleSlider>();
	private JPanel buttonsPane = new JPanel(new GridLayout(0,1));
		private JButton randomAllButton = new JButton("R A N D O M   A L L");
		private JButton randomOneButton = new JButton("R A N D O M   O N E");
		private JButton zeroButton = new JButton("Z E R O");

	private JPanel loadSavePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		private JMenuBar menuBar;
		private JMenu loadMenu;
		private JTextField saveTagField = new JTextField("My Drummer", 15);
		private JButton saveButton = new JButton("Save");
		private JButton deleteButton = new JButton("Delete");
		private JTextField infoLabel = new JTextField(30);
		
	private void initGUI() {
		
		for (Rule rule : ruleManager.getList()) {
			
			RuleSlider slider = new RuleSlider(rule);
			sliderList.add(slider);
			rulesPane.add(slider);
		}
		
		rulesPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane rulesScrollPane = new JScrollPane(rulesPane);
		this.getContentPane().add(rulesScrollPane, BorderLayout.CENTER);
		
		randomAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				randomAllButtonClicked();
			}
		});
		buttonsPane.add(randomAllButton);
		
		randomOneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				randomOneButtonClicked();
			}
		});
		buttonsPane.add(randomOneButton);
		
		zeroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zeroButtonClicked();
			}
		});
		buttonsPane.add(zeroButton);
		
		this.getContentPane().add(buttonsPane, BorderLayout.EAST);
		
		menuBar = new JMenuBar();
		loadMenu = new JMenu("Load");
		menuBar.add(loadMenu);
		updateLoadMenu();
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File saveFile = new File(SAVE_FOLDER + saveTagField.getText().trim());
				if (saveFile.exists()) {
					if (0 == JOptionPane.showConfirmDialog(null,
							"Sure you want to overwrite '"+saveFile.getName()+"'?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
						infoLabel.setText(saveFile.getName()+" overwritten.");
					else {
						infoLabel.setText(saveFile.getName()+" not saved.");
						return;
					}
				}
				else
					infoLabel.setText(saveFile.getName()+" saved.");
				saveToFile(saveFile);
				updateLoadMenu();
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File deleteFile = new File(SAVE_FOLDER + saveTagField.getText().trim());
				if (deleteFile.exists()) {
					if (0 == JOptionPane.showConfirmDialog(null,
							"Sure you want to delete '"+deleteFile.getName()+"'?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
						infoLabel.setText(deleteFile.getName()+" deleted.");
						deleteFile.delete();
						updateLoadMenu();
					}
					else
						infoLabel.setText(deleteFile.getName()+" not deleted.");
				}
				else
					infoLabel.setText(deleteFile.getName()+" does not exist.");
			}
		});
		
		infoLabel.setEditable(false);
		
		loadSavePane.add(menuBar);
		loadSavePane.add(saveTagField);
		loadSavePane.add(saveButton);
		loadSavePane.add(deleteButton);
		loadSavePane.add(infoLabel);
		
		this.getContentPane().add(loadSavePane, BorderLayout.NORTH);
	}
	
	private void updateLoadMenu() {
		
		loadMenu.removeAll();
		
		File saveFolder = new File(SAVE_FOLDER);
		if (!saveFolder.exists())
			saveFolder.mkdir();
		for (File file : saveFolder.listFiles()) {
			JMenuItem item = new JMenuItem(file.getName());
			final File loadFile = file;
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (loadFile.exists()) {
						loadFromFile(loadFile);
						saveTagField.setText(loadFile.getName());
					}
					updateLoadMenu();
				}
			});
			loadMenu.add(item);
		}
		
		loadMenu.addSeparator();
		JMenuItem item = new JMenuItem("update list");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLoadMenu();
			}
		});
		loadMenu.add(item);
		
	}
	
	private void randomOneButtonClicked() {
		
		int randomValue = Random.rangeInt( 0, RuleSlider.sliderFactor);
		this.sliderList.get(Random.nextInt(sliderList.size())).setValue(randomValue);
		
	}



	private void zeroButtonClicked() {
		
		for (RuleSlider slider : this.sliderList) {
			slider.setValue(0);
		}
	}

	private void randomAllButtonClicked() {
		
		for (RuleSlider slider : this.sliderList) {
			slider.setValue(Random.rangeInt( 0, RuleSlider.sliderFactor));
		}
	}

	private void loadFromFile(File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			String line = "";
			while ((line = raf.readLine()) != null) {
				String[] ruleSpec = line.split(":");
				if (ruleSpec.length == 2)
					for (RuleSlider slider : this.sliderList) {
						if (slider.getName().equalsIgnoreCase(ruleSpec[0]))
							slider.setWeight(Float.parseFloat(ruleSpec[1]));
					}
			}
			raf.close();
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	private void saveToFile(File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.setLength(0);
			for (RuleSlider slider : this.sliderList) {
				raf.writeBytes(slider.getName()+":"+slider.getWeight()+"\n");
			}
			raf.close();
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}
	
	private void startSliderColorThread() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				running = true;
				while (running) {
					
					for (RuleSlider ruleSlider : sliderList) {
					
						float color = ruleSlider.getColor();
						if (color > 0) {
							color -= 0.02f;
							color = Math.max(0, color);
							ruleSlider.setColor(color);
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


	@Override
	public void dispose() {
		
		this.running = false;
		super.dispose();
	}
	



	private class RuleSlider extends JPanel implements ChangeListener, Observer {

		private static final long serialVersionUID = -5336910785254529425L;
		
		public static final int sliderFactor = 100;
		private JSlider slider;
		
		private Color hueColor;
		
		private Rule rule;
		
		private RuleSlider(Rule rule) {
			
			this.rule = rule;
			rule.addObserver(this);
			
			slider = new JSlider( 0, sliderFactor, (int) (rule.getWeight()*sliderFactor));
			slider.setToolTipText(rule.getDescription());
			slider.setOrientation(JSlider.VERTICAL);
			slider.setMajorTickSpacing(50);
			slider.setMinorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put( new Integer( sliderFactor ), new JLabel("1.0") );
			labelTable.put( new Integer( 0 ), new JLabel("0") );
			slider.setLabelTable( labelTable );
			slider.addChangeListener(this);
			this.add(slider);
			
			this.setColor(0f);
			
			this.setBorder(BorderFactory.createTitledBorder(rule.getName()));
			
		}

		//0f = green, 1f = red;
		public void setColor(float hue) {
			
			float correctedHue = (1 - hue) * 0.333f;
			
			hueColor = new Color(Color.HSBtoRGB(correctedHue, 1f, 1f));
			
			if (hue != 0f)
				slider.setBackground(hueColor);
			else
				slider.setBackground(Color.BLACK);
		}
		
		public float getColor() {
			Color col = hueColor;
			float hue = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null)[0];
			float color = 1 - (hue / 0.333f);
			return color;
		}

		@Override
		public void stateChanged(ChangeEvent ce) {
			this.rule.setWeight(((float)slider.getValue())/sliderFactor);
			
			if (Settings.DEBUG)
				Streams.ruleOut.println("Rule "+rule.getName()+" weight changed to "+rule.getWeight());
			
		}
		
		public void setValue(int value) {
			this.slider.setValue(value);
		}
		
		public float getWeight() {
			return (float)this.slider.getValue() / sliderFactor;
		}
		
		public void setWeight(float w) {
			this.setValue((int)(w * sliderFactor));
		}

		@Override
		public void update(Observable rule, Object weightedRate) {
			
			float rate = Math.abs((float)weightedRate) / Rule.LIMIT;
			
			if (rule == this.rule) {
				if (rate > this.getColor())
					this.setColor(rate);
			}
			
		}
		
		public String getName() {
			return rule.getName();
		}
		
	}

}
