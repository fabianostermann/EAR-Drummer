package input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InputAnalysis {
	
	//TODO: build GUI to debug
	
	private static long idCounter = 0;
	private long id;
	
	ArrayList<MelodyNote> notes;
	
	//analysis values here
	public int numberOfNotes;
	public int numberOfNotesOctave;
	public int numberOfDifferentNotes;
	public int numberOfDifferentNotesOctave;
	
	public double lengthSD;
	public double lengthAverage;
	public long lengthRange;
	public long lengthMin = Long.MAX_VALUE;
	public long lengthMax = Long.MIN_VALUE;
	
	public double distanceSD;
	public double distanceAverage;
	public long distanceRange;
	public long distanceMin = Long.MAX_VALUE;
	public long distanceMax = Long.MIN_VALUE;
	
	public float keySD;
	public float keyAverage;
	public int keyRange;
	public int keyMin = Integer.MAX_VALUE;
	public int keyMax = Integer.MIN_VALUE;
	
	public float intervalSD;
	public float intervalAverage;
	public int intervalRange;
	public int intervalMin = Integer.MAX_VALUE;
	public int intervalMax = Integer.MIN_VALUE;
	
	public float volumeSD;
	public float volumeAverage;
	public int volumeRange;
	public int volumeMin = Integer.MAX_VALUE;
	public int volumeMax = Integer.MIN_VALUE;
	
	public double gapSD;
	public double gapAverage;
	public long gapRange;
	public long gapMin = Long.MAX_VALUE;
	public long gapMax = Long.MIN_VALUE;
	
	
	public InputAnalysis(ArrayList<MelodyNote> notes) {
		this.notes = notes;
		
		this.id = InputAnalysis.idCounter++;
		
		this.generateStats();
	}
	
	public void generateStats() {
		if (notes == null || notes.size() <= 0)
			return;
		
		Collections.sort(notes);
		
		numberOfNotes = notes.size();
		
		Set<Integer> notesOctaveSet = new HashSet<Integer>();
		Set<Integer> differentNotesSet = new HashSet<Integer>();
		Set<Integer> differentNotesOctaveSet = new HashSet<Integer>();
		
		MelodyNote note;
		MelodyNote next;
		
		long distance;
		int interval;
		long gap;
		
		//analyze here
		for (int i = 0; i < notes.size(); i++) {
			
			note = notes.get(i);
			
			notesOctaveSet.add(note.key % 12);
			differentNotesSet.add(note.key);
			differentNotesOctaveSet.add(note.key % 12);
			
			lengthMin = Math.min(lengthMin, note.length);
			lengthMax = Math.max(lengthMax, note.length);
			lengthAverage += note.length;
			
			keyMin = Math.min(keyMin, note.key);
			keyMax = Math.max(keyMax, note.key);
			keyAverage += note.key;
			
			volumeMin = Math.min(volumeMin, note.volume);
			volumeMax = Math.max(volumeMax, note.volume);
			volumeAverage += note.volume;
			
		}
		
		for (int i = 0; i < notes.size()-1; i++) {
			
			note = notes.get(i);
			next = notes.get(i+1);
			
			distance = next.start - note.start;
			distanceMin = Math.min(distanceMin, distance);
			distanceMax = Math.max(distanceMax, distance);
			distanceAverage += distance;
			
			interval = Math.abs(next.key - note.key);
			intervalMin = Math.min(intervalMin, interval);
			intervalMax = Math.max(intervalMax, interval);
			intervalAverage += interval;
			
			gap = next.start - (note.start + note.length);
			gapMin = Math.min(gapMin, gap);
			gapMax = Math.max(gapMax, gap);
			gapAverage += gap;
		}
		
		numberOfNotesOctave = notesOctaveSet.size();
		numberOfDifferentNotes = differentNotesSet.size();
		numberOfDifferentNotesOctave = differentNotesOctaveSet.size();
		
		lengthRange = lengthMax - lengthMin;
		lengthAverage /= numberOfNotes;
		
		distanceRange = distanceMax - distanceMin;
		distanceAverage /= numberOfNotes-1;
		
		keyRange = keyMax - keyMin;
		keyAverage /= numberOfNotes;
		
		intervalRange = intervalMax - intervalMin;
		intervalAverage /= numberOfNotes-1;
		
		volumeRange = volumeMax - volumeMin;
		volumeAverage /= numberOfNotes;
		
		gapRange = gapMax - gapMin;
		gapAverage /= numberOfNotes-1;
		
		for (int i = 0; i < notes.size(); i++) {
			
			note = notes.get(i);
			
			lengthSD += (note.length - lengthAverage)*(note.length - lengthAverage);
			keySD += (note.key - keyAverage)*(note.key - keyAverage);
			volumeSD += (note.volume - volumeAverage)*(note.volume - volumeAverage);
			
		}
		
		for (int i = 0; i < notes.size()-1; i++) {
			
			note = notes.get(i);
			next = notes.get(i+1);
			
			distance = next.start - note.start;
			distanceSD += (distance - distanceAverage)*(distance - distanceAverage);
			
			interval = Math.abs(next.key - note.key);
			intervalSD += (interval - intervalAverage)*(interval - intervalAverage);
			
			gap = next.start - (note.start + note.length);
			gapSD += (gap - gapAverage)*(gap - gapAverage);
		}
		
		lengthSD /= numberOfNotes-1;
		distanceSD /= numberOfNotes-2;
		keySD /= numberOfNotes-1;
		intervalSD /= numberOfNotes-2;
		volumeSD /= numberOfNotes-1;
		gapSD /= numberOfNotes-2;
		
		lengthSD /= Math.sqrt(lengthSD);
		distanceSD /= Math.sqrt(distanceSD);
		keySD /= Math.sqrt(keySD);
		intervalSD /= Math.sqrt(intervalSD);
		volumeSD /= Math.sqrt(volumeSD);
		gapSD /= Math.sqrt(gapSD);
	}
	
	@Override
	public String toString() {
		String s = "=== InputAnalysis No."+id+" ===\n";
		
		s += "numberOfNotes: "+numberOfNotes+"\n";
		s += "numberOfDifferentNotes: "+numberOfDifferentNotes+"\n";
		
		s += "lengthSD: "+lengthSD+"\n";
		s += "lengthAverage: "+lengthAverage+"\n";
		s += "lengthRange: "+lengthRange+"\n";
		s += "lengthMin: "+lengthMin+"\n";
		s += "lengthMax: "+lengthMax+"\n";
		
		s += "distanceSD: "+distanceSD+"\n";
		s += "distanceAverage: "+distanceAverage+"\n";
		s += "distanceRange: "+distanceRange+"\n";
		s += "distanceMin: "+distanceMin+"\n";
		s += "distanceMax: "+distanceMax+"\n";
		
		s += "keySD: "+keySD+"\n";
		s += "keyAverage: "+keyAverage+"\n";
		s += "keyRange: "+keyRange+"\n";
		s += "keyMin: "+keyMin+"\n";
		s += "keyMax: "+keyMax+"\n";
		
		s += "intervalSD: "+intervalSD+"\n";
		s += "intervalAverage: "+intervalAverage+"\n";
		s += "intervalRange: "+intervalRange+"\n";
		s += "intervalMin: "+intervalMin+"\n";
		s += "intervalMax: "+intervalMax+"\n";
		
		s += "volumeSD: "+volumeSD+"\n";
		s += "volumeAverage: "+volumeAverage+"\n";
		s += "volumeRange: "+volumeRange+"\n";
		s += "volumeMin: "+volumeMin+"\n";
		s += "volumeMax: "+volumeMax+"\n";
		
		s += "gapSD: "+gapSD+"\n";
		s += "gapAverage: "+gapAverage+"\n";
		s += "gapRange: "+gapRange+"\n";
		s += "gapMin: "+gapMin+"\n";
		s += "gapMax: "+gapMax+"\n";
		
		return s;
	}

	public long getId() {
		return id;
	}

}
