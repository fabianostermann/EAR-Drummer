package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameManager extends JFrame {
	
	private static final long serialVersionUID = 8043273266935226000L;
	
	private static final FrameManager frameManager = new FrameManager();
	public static FrameManager getInstance() {
		return frameManager;
	}

	private FrameManager() {
		this.setTitle("Frame Manager");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.initGUI();
		this.pack();
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
	}
	
	public void addFrame(ManagedFrame frame) {
		frameBoxes.add(new FrameBox(frame));
		this.rebuild();
	}
	
	public void deleteFrame(ManagedFrame frame) {
		for (int i = 0; i < frameBoxes.size(); i++) {
			if (frameBoxes.get(i).frame == frame) {
				frameBoxes.remove(i);
				this.rebuild();
				return;
			}
		}
	}
	
	//Window components
	private ArrayList<FrameBox> frameBoxes = new ArrayList<FrameBox>();
	private JPanel frameBoxPane = new JPanel();
	
	private void initGUI() {
		
		frameBoxPane = new JPanel();
		frameBoxPane.setLayout(new BoxLayout(frameBoxPane, BoxLayout.Y_AXIS));
		frameBoxPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.rebuild();
		
		this.getContentPane().add(frameBoxPane, BorderLayout.CENTER);
		
	}
	
	public void rebuild() {
		frameBoxPane.removeAll();
		
		for (FrameBox box : frameBoxes) {
			frameBoxPane.add(box);
		}
		
		this.refresh();
		this.pack();
	}
	
	public void refresh() {
		for (FrameBox box : frameBoxes) {
			box.refresh();
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		//on Window close
		System.exit(0);
	}
	
	private class FrameBox extends JCheckBox implements ActionListener{
		
		private static final long serialVersionUID = 2559528641504691920L;
		
		private ManagedFrame frame;
		
		public FrameBox(ManagedFrame frame) {
			super(frame.getTitle());

			this.frame = frame;
			this.setSelected(frame.isVisible());
			
			this.addActionListener(this);
		}

		public void refresh() {
			this.setSelected(this.frame.isVisible());
			this.setText(this.frame.getTitle());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == this)
				this.frame.setVisible(this.isSelected());
		}
		
	}
}
