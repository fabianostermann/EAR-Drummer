package bass;

public interface SimpleBassist {

	public static final SimpleBassist[] list = {
		new EmptyBassist(),
		new WalkIn4Bassist(),
		new BossaNovaBassist(),
	};

	@Override
	public String toString();
	
	public Integer nextNote(int barCount, int currTick, int numOfTicks, ChordLib.ChordTable table);
	
}
