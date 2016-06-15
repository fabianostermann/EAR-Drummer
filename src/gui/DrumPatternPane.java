package gui;

import genetic.DrumPattern;
import genetic.RhythmNote;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class DrumPatternPane extends JPanel implements KeyListener{

	private String[] instruments = RhythmNote.drumNames;
	
	private DrumPattern pattern;
	
	private JTextField[][] elements;
	
	public DrumPatternPane(DrumPattern pattern) {
		this.pattern = pattern;
		
		this.elements = new JTextField[pattern.getTicks()][pattern.getInstruments()];
		
		//initiate visuals
		this.setLayout(new GridLayout(pattern.getInstruments(),pattern.getTicks()));
		
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[i].length; j++) {
				elements[i][j] = new JTextField(""+pattern.get(i, j), 3);
				elements[i][j].setBackground(new Color(0f,((float)pattern.get(i,j))/128f,0f));
				elements[i][j].addKeyListener(this);
				elements[i][j].addMouseListener(popupListener);
			}
		
		for (int j = 0; j < elements[j].length; j++) {
			
			if (j < instruments.length)
				this.add(new JLabel(instruments[j]));
			else
				this.add(new JLabel("unknown"));
			
			for (int i = 0; i < elements.length; i++) {
					this.add(elements[i][j]);
			}
		}
	}
	
	public void setPattern(DrumPattern pattern) {
		this.pattern = pattern;
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[i].length; j++) {
				elements[i][j].setText(""+pattern.get(i, j));
				elements[i][j].setBackground(new Color(0f,((float)pattern.get(i,j))/128f,0f));
			}
	}
	
	public void highlightColumn(int column) {
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[i].length; j++) {
				if (i == column) {
					elements[i][j].setBackground(new Color(0.8f,((float)pattern.get(i,j))/128f,0f));
				} else {
					elements[i][j].setBackground(new Color(0f,((float)pattern.get(i,j))/128f,0f));
				}
			}
	}
	
	
	
	private boolean editable = true;
	public void setEditable(boolean b) {
		editable = b;
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[i].length; j++) {
				elements[i][j].setEditable(b);
			}
	}
	
	public boolean isEditable() {
		return this.editable;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			return;
		
		updateElements();
		
	}
	
	public void updateElements() {
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[i].length; j++) {
				
				try {
				pattern.set(i, j, Integer.parseInt(elements[i][j].getText()));
				}
				catch (NumberFormatException ex) {}
				finally {
					elements[i][j].setText(""+pattern.get(i, j));
					elements[i][j].setBackground(new Color(0f,((float)pattern.get(i,j))/128f,0f));
				}
			}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	
	//Popup section
	private PopupListener popupListener = new PopupListener(this);
	
	class PopupListener extends MouseAdapter {

		private int[] popupValues = new int[] { 0, 50, 60, 70, 80, 90, 100, 110, 120, 125, 127 };
		
		DrumPatternPane parent;
		
		public PopupListener(DrumPatternPane parent) {
			this.parent = parent;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			popupRequested(e);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			popupRequested(e);
		}

		private void popupRequested(MouseEvent e) {
			if (e.isPopupTrigger()) {
				JPopupMenu popup = new JPopupMenu();
				final JTextField element = (JTextField)e.getComponent();
				
				if (element.isEditable()) {
					
					for (int i : popupValues) {
						JMenuItem m = new JMenuItem(""+i);
						m.setBackground(new Color(0f,(float)i / 128f,0f));
						m.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								element.setText(((JMenuItem)e.getSource()).getText());
								parent.updateElements();
							}
						});
						popup.add(m);
					}
					
			        popup.show(element, e.getX(), e.getY());
				}
			}
		}
		
	}
	
}
