package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MidiManagerFrame extends ManagedFrame {

	private static final int FRAME_WIDTH = 400;

	public MidiManagerFrame(ConsoleArea console, JPanel... panes) {
		
		this.setTitle("Midi Manager");
		
		JPanel managerPane = new JPanel();
		managerPane.setLayout(new BoxLayout(managerPane, BoxLayout.PAGE_AXIS));
		for (JPanel pane : panes) {
			pane.setPreferredSize(new Dimension(FRAME_WIDTH, 200));
			managerPane.add(pane);
		}
		this.getContentPane().add(managerPane, BorderLayout.NORTH);
		
		// setup console
		console.setEditable(false);
		console.log("Midi Manager Console initialized.");
		
		// TODO DEPRECATED: it is enough that console output goes to debug/error stream
		//JScrollPane scrollPaneConsole = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//this.getContentPane().add(scrollPaneConsole, BorderLayout.CENTER);
		
		// setup frame style
		//this.setSize(FRAME_WIDTH,600);
		
		this.pack();
		this.setResizable(false);

		this.setLocationByPlatform(true);
		this.setVisible(false);
		
	}

}
