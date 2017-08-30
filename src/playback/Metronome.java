package playback;

import java.util.Vector;

public class Metronome implements Runnable{

	private Vector<MetronomeListener> listenerList = new Vector<>();
	
	public class Settings implements Cloneable{
		public int tpm;
		public float shuffle;
		
		public Settings() {}
		public Settings(int tpm, float shuffle) {
			this.tpm = tpm;
			this.shuffle = shuffle;
		}
		
		@Override
		protected Settings clone() {
			Settings clone = new Settings(this.tpm, this.shuffle);
			return clone;
		}
		
		@Override
		public String toString() {
			return "(tpm=" + tpm + ", shuffle=" + shuffle + ")";
		}
	}
	
	public int ticks;
	
	private Settings settings;
	
	private int durationOnBeat;
	private int durationOffBeat;
	private boolean offBeat;
	
	private int tick = 0;
	
	private boolean running = false;
	
	public Metronome(int ticks) {
		this.ticks = ticks;
		this.settings = new Settings();
		this.settings.tpm = 200;
		this.settings.shuffle = 0.5f;
		recalculateDuration();
	}
	
	public Metronome(int ticks, int tpm, float shuffle) {
		this.ticks = ticks;
		this.settings = new Settings();
		this.settings.tpm = tpm;
		this.settings.shuffle = shuffle;
		recalculateDuration();
	}
	
	public void start() {
		if (!isRunning()) {
			tick = 0;
			offBeat = false;
			running = true;
			new Thread(this).start();
		}
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		
		long startTime, sleepTime, deltaTime;
		
		while(running) {
			startTime = System.currentTimeMillis();
			
			//inform listeners
			for (MetronomeListener listener : this.listenerList) {
				listener.tick(this);
			}
			
			//set time to sleep
			if (offBeat) {
				sleepTime = durationOffBeat;
				offBeat = false;
			} else {
				sleepTime = durationOnBeat;
				offBeat = true;
			}
			
			try { //sleep
				deltaTime = System.currentTimeMillis()-startTime;
				if (sleepTime - deltaTime > 0)
					Thread.sleep(sleepTime - deltaTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			increaseTick();
		}
	}
	
	private void increaseTick() {
		this.tick++;
		if (this.tick >= this.ticks)
			this.tick = 0;
	}
	
	private void recalculateDuration() {
		this.durationOnBeat = (int)((float)(2 * this.settings.shuffle * 60000) / this.settings.tpm);
		this.durationOffBeat = (int)((float)(2 * (1 - this.settings.shuffle) * 60000) / this.settings.tpm);
	}

	public void addMetronomeListener(MetronomeListener listener) {
		this.listenerList.add(listener);
	}

	public int getTick() {
		return tick;
	}
	public int getTicks() {
		return this.ticks;
	}

	public int getTpm() {
		return this.settings.tpm;
	}

	public void setTpm(int tpm) {
		this.settings.tpm = tpm;
		this.recalculateDuration();
	}

	public float getShuffle() {
		return this.settings.shuffle;
	}

	public void setShuffle(float shuffle) {
		this.settings.shuffle = shuffle;
		this.recalculateDuration();
	}
	
	public boolean isRunning() {
		return running;
	}

	public Settings getSettingsClone() {
		return this.settings.clone();
	}
	
	/**
	 * Sets the settings from an object,
	 * but does not update ticks!
	 */
	public void setSettings(Metronome.Settings newSettings) {
		this.setTpm(newSettings.tpm);
		this.setShuffle(newSettings.shuffle);
	}
}
