package gui;

import init.ImageLoader;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import bass.PrimitiveBassist;


@SuppressWarnings("serial")
public class PrimitiveBassistFrame extends ManagedFrame {

	private PrimitiveBassist bassist;

	public PrimitiveBassistFrame(PrimitiveBassist bassist) {

		if (bassist == null)
			throw new NullPointerException();

		this.bassist = bassist;

		this.setTitle("Primitive Bassist");

		this.initGUI();
		this.pack();

		this.setLocationByPlatform(true);
		this.setVisible(true);

	}

	// Window components
	private JButton resetButton;
	private JButton enableButton;

	private void initGUI() {

		resetButton = ImageLoader.createButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bassist.reset();
			}
		});
		
		enableButton = ImageLoader.createButton("");
		if (bassist.isEnabled()) {
			enableButton.setText("Disable");
		} else {
			enableButton.setText("Enable");
		}
		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bassist.isEnabled()) {
					bassist.setEnabled(false);
					enableButton.setText("Enable");
				} else {
					bassist.setEnabled(true);
					enableButton.setText("Disable");
				}
			}
		});
		
//		enableButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//		resetButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(enableButton);
		this.getContentPane().add(resetButton);
	}

}
