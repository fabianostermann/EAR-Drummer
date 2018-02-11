package gui;

import init.ImageLoader;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class StartupDialog extends JDialog {

	/** Value must be adjusted to match number of incProgress() invocations */
	private static final int PROG_MAX = 15;
	
	private JPanel infoPane = new JPanel();
	private JProgressBar progBar = new JProgressBar(JProgressBar.HORIZONTAL);
	
	public StartupDialog() {
		this.setUndecorated(true);
		
		infoPane.add(new JLabel(ImageLoader.getLogo()));
		
		progBar.setIndeterminate(true);
		progBar.setMinimum(0);
		progBar.setMaximum(PROG_MAX);
		
		this.add(infoPane, BorderLayout.CENTER);
		this.add(progBar, BorderLayout.SOUTH);
		
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}
	
	public void incProgress(String infoText) {
		progBar.setIndeterminate(false);
		if (infoText == null || infoText.isEmpty())
			progBar.setStringPainted(false);
		else {
			progBar.setStringPainted(true);
			progBar.setString(infoText);
		}
		progBar.setValue(progBar.getValue()+1);
	}

	public void endProgress(String infoText) {
		incProgress(infoText);
		progBar.setValue(PROG_MAX);

		try { Thread.sleep(500); }
		catch (Exception e) {}//ignore
		
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
