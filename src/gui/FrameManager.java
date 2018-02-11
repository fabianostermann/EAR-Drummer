package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FrameManager extends JFrame implements WindowStateListener {
	
	private static FrameManager instance = new FrameManager();

	private FrameManager() {
		this.setTitle("Frame Manager");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.initGUI();
		this.pack();
		this.setResizable(false);
		
		this.setLocationByPlatform(true);
		this.setVisible(false);
		
		this.addWindowStateListener(this);
	}
	
	public static void showAll() {
		instance.setVisible(true);
		for (FrameBox box : instance.frameBoxes) {
			box.frame.setVisible(true);
		}
	}
	
	public static void addFrame(ManagedFrame frame) {
		instance.frameBoxes.add(instance.new FrameBox(frame));
		instance.rebuild();
	}
	
	public static void deleteFrame(ManagedFrame frame) {
		for (int i = 0; i < instance.frameBoxes.size(); i++) {
			if (instance.frameBoxes.get(i).frame == frame) {
				instance.frameBoxes.remove(i);
				instance.rebuild();
				return;
			}
		}
	}
	
	//Window components
	private ArrayList<FrameBox> frameBoxes = new ArrayList<FrameBox>();
	private JPanel frameBoxPane = new JPanel();
	
	private void initGUI() {

		frameBoxPane.setLayout(new BoxLayout(frameBoxPane, BoxLayout.Y_AXIS));
		frameBoxPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 50));
		
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
	
	public static void staticRefresh() {
		instance.refresh();
	}
	
	/**
	 * try to place all frames on one screen
	 */
	public static void rearrangeFrames() {
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle screenBounds = env.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		
		int xLocation = screenBounds.x + 10, yLocation = screenBounds.y + 10, maxHeight = 0;
		
		instance.setLocation(xLocation, yLocation);
		xLocation = instance.getLocation().x + instance.getWidth() + 10;
		maxHeight = instance.getHeight();
		for (FrameBox frameBox : instance.frameBoxes) {
			if (xLocation + frameBox.frame.getWidth() + 10 > screenBounds.width) {
				xLocation = screenBounds.x + 10;
				yLocation += maxHeight + 20;
				maxHeight = 0;
			}
			if (yLocation + frameBox.frame.getHeight() > screenBounds.height) {
				yLocation = screenBounds.y + 10;
				System.err.println("Could not arrange windows probably, screen size to small.");
			}
			frameBox.frame.setLocation(xLocation, yLocation);
			maxHeight = Math.max(frameBox.frame.getHeight(), maxHeight);
			xLocation += frameBox.frame.getWidth() + 10;
		}
	}
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		if (e.getNewState() == JFrame.ICONIFIED){
			for (FrameBox box : instance.frameBoxes) {
				box.frame.setMinimized(true);
			}
		}
		else if (e.getNewState() == JFrame.NORMAL){
			for (FrameBox box : instance.frameBoxes) {
				box.frame.setMinimized(false);
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		//on Window close
		System.exit(0);
	}

	private class FrameBox extends JCheckBox implements ActionListener{
		
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
