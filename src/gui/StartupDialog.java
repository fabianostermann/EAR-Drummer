package gui;

import init.ImageLoader;
import init.Settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class StartupDialog extends JDialog {

	/** Value must be adjusted to match number of incProgress() invocations */
	private static final int PROG_MAX = 15;
	
	private JPanel infoPane = new JPanel();
	private JProgressBar progBar = new JProgressBar(JProgressBar.HORIZONTAL);
	
	public StartupDialog() {
		this.setUndecorated(true);
		
		infoPane.add(new JLabel(ImageLoader.getLogo()));
		
		Border border = BorderFactory.createEtchedBorder();
		border = BorderFactory.createTitledBorder(
				border,
				Settings.TITLE,
				TitledBorder.LEADING, TitledBorder.TOP,
				new Font("",Font.BOLD, 24), Color.BLACK);
		border = BorderFactory.createTitledBorder(
				border,
				Settings.VERSION,
				TitledBorder.TRAILING, TitledBorder.TOP,
				new Font("",Font.BOLD, 20), Color.BLACK);
		border = BorderFactory.createTitledBorder(
				border,
				"by "+Settings.AUTHOR,
				TitledBorder.TRAILING, TitledBorder.BOTTOM,
				new Font("",Font.ITALIC, 12), Color.BLACK);
		infoPane.setBorder(border);
		
		progBar.setIndeterminate(true);
		progBar.setMinimum(0);
		progBar.setMaximum(PROG_MAX);
		
		this.add(infoPane, BorderLayout.CENTER);
		this.add(progBar, BorderLayout.SOUTH);
		
		this.pack();
		this.setLocationRelativeTo(null);
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
		
		dispose();
	}

	@Override
	public void dispose() {
		try { Thread.sleep(500); }//wait 500ms
		catch (Exception e) {}//ignore
		super.dispose();
	}

}
