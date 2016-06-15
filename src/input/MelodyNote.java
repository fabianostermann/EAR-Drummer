package input;

public class MelodyNote implements Comparable<MelodyNote> {

	public int key;
	public int volume;
	public long start;
	public long length;
	
	public MelodyNote(int key, int volume, long start, long length) {
		this.key = key;
		this.volume = volume;
		this.start = start;
		this.length = length;
	}
	
	@Override
	public String toString() {
		return "[key="+key+",vol="+volume+","+start+"-"+length+"]";
	}

	@Override
	public int compareTo(MelodyNote note) {
		return (int) (this.start - note.start);
	}
	
}
