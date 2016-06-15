package gui;

import javax.swing.JFrame;

public class ManagedFrame extends JFrame {

	private static final long serialVersionUID = -1076179390331059000L;

	public ManagedFrame() {
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		FrameManager.getInstance().addFrame(this);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			this.setState(JFrame.NORMAL);
		}
		FrameManager.getInstance().refresh();
	}
	
	@Override
	public void dispose() {
		FrameManager.getInstance().deleteFrame(this);
		super.dispose();
	}
	
}
