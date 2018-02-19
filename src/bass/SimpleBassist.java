package bass;

public interface SimpleBassist {

	public static final SimpleBassist[] list = {
		new WalkIn4Bassist(),
		new BossaNovaBassist(),
		new EmptyBassist(),
	};

	@Override
	public String toString();
	
	public Integer nextNote(int barCount, int currTick, int numOfTicks, ChordLib.ChordTable table);
	
}
