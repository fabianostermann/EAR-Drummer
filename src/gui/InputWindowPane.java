package gui;

import input.InputWindow;
import input.MelodyNote;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InputWindowPane extends JPanel implements Observer, MouseMotionListener, MouseWheelListener {

	private int heightScale = 128;
	private int widthScale = 0;
	
	private long start;
	private long end;
	
	private float zoom = 1.0f;
	private int offsetX = 0;
	
	private ArrayList<MelodyNote> notes;
	
	
	private InputWindow inputWindow;
	
	public InputWindowPane(InputWindow inputWindow) {
		this.inputWindow = inputWindow;
		inputWindow.addObserver(this);
		
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		
		update(inputWindow, null);
	}
	
	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		g.setColor(Color.GRAY);
		g.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight());
		g.drawLine(0, 0, this.getWidth(), 0);
		
		if (notes.isEmpty())
			return;
		
		heightScale = 128;
		widthScale = (int)(end - start);
		
		int x1,x2,y;
		for (MelodyNote note : notes) {
			
			g.setColor(new Color(0f,((float)note.volume)/128f,0f));
			
			x1 = (int)(note.start - start) * this.getWidth() / widthScale;
			x2 = (int)(note.start + note.length - start) * this.getWidth() / widthScale;
			y = this.getHeight() - (note.key * this.getHeight() / heightScale);
			
			x1 += offsetX;
			x2 += offsetX;
			
			x1 *= zoom;
			x2 *= zoom;
			
			g.fillRect(x1, y, x2-x1, this.getHeight()/128);
		}

	}

	@Override
	public void update(Observable o, Object obj) {
		
		if (o != inputWindow)
			return;
		
		notes = new ArrayList<MelodyNote>(inputWindow.getNotesCopy());
		
		if (!notes.isEmpty()) {
			//find start and end time
			start = notes.get(0).start;
			end = notes.get(0).start + notes.get(0).length;
			for (MelodyNote note : notes) {
				start = Math.min(start, note.start);
				end = Math.max(end, note.start + note.length);
			}
		}
		
		repaint();
	}

	
	private int oldXPosition = 0;
	@Override
	public void mouseDragged(MouseEvent e) {
		offsetX -= oldXPosition - e.getX();
		
		oldXPosition = e.getX();
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		oldXPosition = e.getX();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		zoom -= (float)(e.getWheelRotation())/10;
		
		zoom = Math.max(0.1f, zoom);
		zoom = Math.min(100.0f, zoom);
		
		repaint();
	}

}
