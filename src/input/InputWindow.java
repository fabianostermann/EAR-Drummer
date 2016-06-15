package input;

import java.util.ArrayList;
import java.util.Observable;

public class InputWindow extends Observable{
	
	private ArrayList<MelodyNote> notes = new ArrayList<MelodyNote>();
	
	public synchronized void addNote(MelodyNote note) {
		notes.add(note);
		
		setChanged();
		notifyObservers();
	}
	
	public synchronized void clear() {
		notes.clear();
		
		setChanged();
		notifyObservers();
	}
	
	public synchronized void clearOlder(long now, long window) {
		
		long delta = now - window;
		for (int i = notes.size()-1; i >= 0; i--) {
			if (notes.get(i).start < delta) {
				notes.remove(i);
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	public synchronized ArrayList<MelodyNote> getNotesCopy() {
		return new ArrayList<MelodyNote>(notes);
	}
	
	@Override
	public synchronized String toString() {
		String s = "InputWindow_size="+notes.size()+"[";
		for (MelodyNote note : notes) {
			s += note.toString();
		}
		return s + "]";
	}

}
