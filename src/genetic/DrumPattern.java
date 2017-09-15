package genetic;


public class DrumPattern extends Rateable {
	
	
	
	public int[][] matrix;
	
	public DrumPattern(int ticks) {
		matrix = new int[ticks][RhythmNote.NUMBER_OF_DRUMS];
	}
	
	public void set(int tick, int instrument, int volume) {
		volume = Math.max(0, volume);
		volume = Math.min(volume, 127);
		matrix[tick][instrument] = volume;
	}
	
	public void remove(int tick, int instrument) {
		matrix[tick][instrument] = 0;
	}
	
	public int get(int tick, int instrument) {
		return matrix[tick][instrument];
	}
	
	public int getTicks() {
		return matrix.length;
	}
	
	public int getInstruments() {
		return matrix[0].length;
	}
	
	public DrumPattern copy() {
		DrumPattern clone = new DrumPattern(this.getTicks());
		clone.matrix = new int[this.getTicks()][this.getInstruments()];
		for (int i = 0; i < this.getTicks(); i++)
			for (int j = 0; j < this.getInstruments(); j++)
				clone.matrix[i][j] = this.matrix[i][j];
		return clone;
	}
	
	public int getActivation() {
		int activation = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				activation += matrix[i][j];
			}
		}
		return activation;
	}
	
	@Override
	public String toString() {
		return super.toString()+"[activation="+this.getActivation()+"]";
	}
}
