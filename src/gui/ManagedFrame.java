package gui;

import javax.swing.JFrame;

public class ManagedFrame extends JFrame {

	private static final long serialVersionUID = -1076179390331059000L;

	public ManagedFrame() {
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		FrameManager.addFrame(this);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			this.setState(JFrame.NORMAL);
		}
		FrameManager.staticRefresh();
	}
	
	@Override
	public void dispose() {
		FrameManager.deleteFrame(this);
		super.dispose();
	}
	
}
