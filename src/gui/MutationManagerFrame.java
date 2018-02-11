package gui;

import genetic.Random;
import genetic.mutations.Mutation;
import genetic.mutations.MutationManager;
import init.ImageLoader;
import init.Streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MutationManagerFrame extends ManagedFrame {

	private MutationManager mutationManager;
	
	public MutationManagerFrame(MutationManager MutationManager) {
		
		this.mutationManager = MutationManager;
		
		this.setTitle("Mutation Manager");
		
		this.initGUI();
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(false);
	}

	//Window components
	private JPanel mutationsPane = new JPanel(new GridLayout(1,0));
		private ArrayList<MutationSlider> sliderList = new ArrayList<MutationSlider>();
	private JPanel buttonsPane = new JPanel(new GridLayout(0,1));
		private JButton randomAllButton = ImageLoader.createButton("R A N D O M   A L L");
		private JButton randomOneButton = ImageLoader.createButton("R A N D O M   O N E");
		private JButton zeroButton = ImageLoader.createButton("Z E R O");
	
	private void initGUI() {
		
		for (Mutation mutation : mutationManager.getList()) {
			
			MutationSlider slider = new MutationSlider(mutation);
			sliderList.add(slider);
			mutationsPane.add(slider);
		}
		
		mutationsPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane rulesScrollPane = new JScrollPane(mutationsPane);
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
	}
	
	
	
	protected void randomOneButtonClicked() {
		
		int randomValue = Random.rangeInt(0, MutationSlider.LIMIT);
		this.sliderList.get(Random.nextInt(sliderList.size())).setValue(randomValue);
		
	}



	private void zeroButtonClicked() {
		
		for (MutationSlider slider : this.sliderList) {
			slider.setValue(0);
		}
	}



	private void randomAllButtonClicked() {
		
		for (MutationSlider slider : this.sliderList) {
			slider.setValue(Random.rangeInt(0, MutationSlider.LIMIT));
		}
	}
	

	private class MutationSlider extends JPanel implements ChangeListener {
		
		private final static int LIMIT = 100;
		
		private JSlider slider;
		
		private Mutation mutation;
		
		private MutationSlider(Mutation mutation) {
			
			this.mutation = mutation;
			
			slider = new JSlider(0, LIMIT, mutation.getWeight());
			slider.setToolTipText(mutation.getDescription());
			slider.setOrientation(JSlider.VERTICAL);
			slider.setMajorTickSpacing(50);
			slider.setMinorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put( new Integer( LIMIT ), new JLabel("100") );
			labelTable.put( new Integer( 0 ), new JLabel("0") );
			slider.setLabelTable( labelTable );
			slider.addChangeListener(this);
			updateColor();
			this.add(slider);
			
			this.setBorder(BorderFactory.createTitledBorder(mutation.getName()));
		}

		@Override
		public void stateChanged(ChangeEvent ce) {
			this.mutation.setWeight(slider.getValue());
			
			Streams.mutationOut.println("Mutation "+mutation.getName()+" weight changed to "+mutation.getWeight());
			
			updateColor();
		}
		
		public void setValue(int value) {
			this.slider.setValue(value);
		}
		
		public void updateColor() {
			
			if (this.mutation.getWeight() <= 0)
				slider.setBackground(Color.BLACK);
			else
				slider.setBackground(new Color(1f, 1f, 1f, 1f));

		}
	}

}
